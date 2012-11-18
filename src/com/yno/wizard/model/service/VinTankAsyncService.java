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
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

import android.os.AsyncTask;
import android.util.Log;

public class VinTankAsyncService extends AsyncTask<AsyncServiceParcel, Void, AsyncServiceParcel> {
	
	public static final String TAG = "VinTankAsyncService";
	public static final String API_ID = "vintank";
	
	private static final String _API_URL = "http://apiv1.cruvee.com";
	private static final String _API_KEY = "898153ea1edb4e6eb9c44033670a5299";
	private static final String _API_SECRET = "3466c4e402a34b4da490de8c2f11ba49";
	private static final String _SERVICE_PRODUCERS = "/search/brands";
	private static final String _SERVICE_REGIONS = "/search/regions";
	private static final String _SERVICE_WINES = "/search/wines";
	private static final String _SERVICE_VARIETALS = "/search/varieties";
	private static final String _QUERY_RESULTS = "results";
	
	private IServiceContext _context;
	public ArrayList<WineParcel> lastUnqualified = new ArrayList<WineParcel>();
	public ArrayList<WineParcel> lastQualified = new ArrayList<WineParcel>();
	
	
	public VinTankAsyncService( IServiceContext $context ){
		_context = $context;
		try{
			Class.forName("android.os.AsyncTask");
		}catch(ClassNotFoundException $e){
			$e.printStackTrace();
		}
	}
	
	
	public void getWinesByQuery(SearchWinesParcel $parcel) {
		
		lastUnqualified.clear();
		lastQualified.clear();
		
		Log.d(TAG, "getWinesByQuery = " + $parcel.getQuery() );
		execute( new AsyncServiceParcel[]{ getSearchInstance( _SERVICE_WINES, $parcel.getQuery(), $parcel.page, SearchData.API_WINE_RESULTS ) } );
	}
	

	@Override
	protected AsyncServiceParcel doInBackground(AsyncServiceParcel... $parcels) {
		
		
		AsyncServiceParcel resultPrcl = new AsyncServiceParcel();
		
		for (AsyncServiceParcel parcel : $parcels){
			resultPrcl.query = parcel.query;
			resultPrcl.svc = parcel.svc;
			resultPrcl.url = parcel.url;
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
		Log.d(TAG, "returning " + resultPrcl.query);
		return resultPrcl;
	}
	
	
	@Override
	protected void onPostExecute(AsyncServiceParcel $parcel) {
		
		WineParcel wine;
		Log.d(TAG, "post");
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
			Log.d(TAG, "No wines found for query " + $parcel.query );
		}
		
		_context.resume(API_ID);

	}
	
	private AsyncServiceParcel getSearchInstance( String $svc, String $query, int $page, int $results ){
		String svcUrl = _API_URL 
				+ $svc + "?q=" + URLEncoder.encode( $query )
				+ "&page=" + $page
				+ "&rpp=" + $results
				+ "&fmt=json";
		AsyncServiceParcel parcel = new AsyncServiceParcel();
		parcel.svc = $svc;
		parcel.url = svcUrl;
		parcel.query = $query;
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
