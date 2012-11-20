package com.yno.wizard.model.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.VarietalFactory;
import com.yno.wizard.model.VarietalParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.TextSearchServiceHelper;

import android.os.AsyncTask;
import android.util.Log;

public class VinTankAsyncService extends AsyncTask<AsyncServiceParcel, Void, AsyncServiceParcel> {
	
	public static final String TAG = "VinTankAsyncService";
	public static final String API_ID = "vintank";
	public static final int SERVICE_PHRASE_ID = 1;
	public static final int SERVICE_WINES_ID = 2;
	public static final int SERVICE_VARIETALS_ID = 3;
	
	private static final String _API_URL = "http://apiv1.cruvee.com";
	private static final String _API_KEY = "898153ea1edb4e6eb9c44033670a5299";
	private static final String _API_SECRET = "3466c4e402a34b4da490de8c2f11ba49";
	private static final String _SERVICE_PRODUCERS = "/search/brands";
	private static final String _SERVICE_REGIONS = "/search/regions";
	private static final String _SERVICE_WINES = "/search/wines";
	private static final String _SERVICE_VARIETALS = "/search/varieties";
	
	
	private static final String _QUERY_RESULTS = "results";
	
	private IServiceContext _context;
	
	public ArrayList<String> phraseData = new ArrayList<String>();
	public ArrayList<WineParcel> lastUnqualified = new ArrayList<WineParcel>();
	public ArrayList<WineParcel> lastQualified = new ArrayList<WineParcel>();
	
	
	public VinTankAsyncService( IServiceContext $context ){
		_context = $context;
	}
	
	
	public void getWineNamesByQuery( String $query, int $page, int $results ){
		execute( new AsyncServiceParcel[]{ getSearchInstance( SERVICE_PHRASE_ID, _SERVICE_WINES, $query , $page, $results ) } );
	}
	
	public void getWinesByQuery(SearchWinesParcel $parcel) {
		//cancel(true);
		lastUnqualified.clear();
		lastQualified.clear();
		
		//Log.d(TAG, "getWinesByQuery = " + $parcel.getQuery() );
		execute( new AsyncServiceParcel[]{ getSearchInstance( SERVICE_WINES_ID, _SERVICE_WINES, $parcel.getQuery(), $parcel.page, SearchData.API_WINE_RESULTS ) } );
	}
	
	public void getVarietalsByQuery( String $query, int $page, int $results ){
				
		execute( new AsyncServiceParcel[]{ getSearchInstance( SERVICE_VARIETALS_ID, _SERVICE_VARIETALS, $query, $page, $results ) } );

	}
	

	@Override
	protected AsyncServiceParcel doInBackground(AsyncServiceParcel... $parcels) {
		
		AsyncServiceParcel resultPrcl = new AsyncServiceParcel();
		
		for (AsyncServiceParcel parcel : $parcels){
			resultPrcl.query = parcel.query;
			resultPrcl.svc = parcel.svc;
			resultPrcl.url = parcel.url;
			resultPrcl.svc_id = parcel.svc_id;
			resultPrcl.page = parcel.page;
			HttpClient httpClient = SearchData.getHttpClient();
			HttpRequestBase httpReq = new HttpGet( parcel.url );
			httpReq.addHeader( "Authorization", getSignature( parcel.svc ) );
			HttpResponse resp;
			try{
				resp = httpClient.execute( httpReq );
				resultPrcl.result  = EntityUtils.toString( resp.getEntity() );
			}catch( IOException $e ){
				$e.printStackTrace();
				resultPrcl.result = "failed";
			}
			
		}
		//Log.d(TAG, "returning " + resultPrcl.query);
		return resultPrcl;
	}
	
	
	@Override
	protected void onPostExecute(AsyncServiceParcel $parcel) {
		
		WineParcel wine;
		//Log.d(TAG, "post");
		if( $parcel.svc_id==SERVICE_WINES_ID ){
			try{
				JSONObject resultObj = new JSONObject( $parcel.result );
				JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createVinTankWine( listAry.getJSONObject(a) );
					if( WineFactory.isQualified( wine, $parcel.query ) )
						lastQualified.add( wine );
					else
						lastUnqualified.add( wine );
				}
			}catch( JSONException $e ){
				//Log.d(TAG, "No wines found for query " + $parcel.query );
			}
			_context.resume($parcel);
		}else if( $parcel.svc_id==SERVICE_PHRASE_ID ){
			
			PhraseServiceParcel pPrcl = new PhraseServiceParcel();
			pPrcl.app_id = $parcel.app_id;
			pPrcl.page = $parcel.page;
			pPrcl.query = $parcel.query;
			pPrcl.result = $parcel.result;
			pPrcl.svc = $parcel.svc;
			pPrcl.svc_id = $parcel.svc_id;
			pPrcl.url = $parcel.url;
			
			// wine names
			if( pPrcl.svc.equals(_SERVICE_WINES) ){
				try{
					JSONObject resultObj = new JSONObject( pPrcl.result );
					JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
					for( int a=0, l= listAry.length(); a<l; a++ )
						pPrcl.values.add( WineFactory.createVinTankWineName(listAry.getJSONObject(a)) );
					
				}catch( JSONException $e ){
					//Log.d(TAG, "No wine names found for query " + $query );
				}
				_context.resume( pPrcl );
			//varietals
			}else{
				try{
					ArrayList<VarietalParcel> vars = new ArrayList<VarietalParcel>(); 
					JSONObject resultObj = new JSONObject( $parcel.result );
					JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
					for( int a=0, l= listAry.length(); a<l; a++ ){
						vars.add( VarietalFactory.createVinTankVarietal( listAry.getJSONObject(a) ) );
					}
				}catch( JSONException $e ){
					//Log.d(TAG, "No varietals found for query " + $query );
				}
			}
		}
		
		

	}
	
	private AsyncServiceParcel getSearchInstance( int $svcId, String $svc, String $query, int $page, int $results ){
		String svcUrl = _API_URL 
				+ $svc + "?q=" + URLEncoder.encode( $query )
				+ "&page=" + $page
				+ "&rpp=" + $results
				+ "&fmt=json";
		AsyncServiceParcel parcel = new AsyncServiceParcel();
		parcel.app_id = API_ID;
		parcel.svc_id = $svcId;
		parcel.svc = $svc;
		parcel.url = svcUrl;
		parcel.query = $query;
		parcel.page = $page;
		return parcel;
	}
	
	private String getSignature( String $service ){
		String hash = "";
		Date now = new Date();
		String ms = String.valueOf( now.getTime() );
		String auth =  _API_KEY + "\n";
		auth += "GET\n";
		auth += _API_SECRET + "\n";
		auth += ms + "\n";
		auth += $service + "\n";
		auth = auth.toLowerCase();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(auth.getBytes());
			byte[] mdb = md.digest();
			int l = mdb.length;
			StringBuilder sb = new StringBuilder( l << 1 );
			for( int a=0;a<l;a++ ){
				sb.append(Character.forDigit((mdb[a] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(mdb[a] & 0x0f, 16));
			}
			hash = sb.toString();
		}catch( NoSuchAlgorithmException $e ){
			//Log.e(TAG, "doVinTankSearch unable to hash MD5: " + $e.toString() );
		}
		return "Cruvee appId=" + _API_KEY + ", sig=" + hash + ", timestamp=" + ms + ", uri=" + $service;
		//return hash;
	}

}
