package com.yno.wizard.model.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
import com.yno.wizard.view.TextSearchServiceHelper;

import android.os.AsyncTask;

public class SnoothAsyncService extends AsyncTask<AsyncServiceParcel, Void, AsyncServiceParcel>{
	
	public static final String TAG = "SnoothAsyncService";
	public static final String API_ID = "snooth";
	
	private static final String _API_URL = "http://api.snooth.com/";
	private static final String _API_KEY = "fcgnjdqjj7116z3uakkpgr20wl24mwwuiwux67bov1bnyvzo";
	private static final String _SERVICE_WINES = "wines/";
	private static final String _FIELD_META = "meta";
	private static final String _FIELD_RETURNED = "returned";
	private static final String _FIELD_WINES = "wines";
	
	private TextSearchServiceHelper _context;
	private SearchWinesParcel _parcel;
	public ArrayList<WineParcel> lastUnqualified = new ArrayList<WineParcel>();
	public ArrayList<WineParcel> lastQualified = new ArrayList<WineParcel>();
	
	
	public SnoothAsyncService( TextSearchServiceHelper $context	 ){
		_context = $context;
	}


	//@Override
	public void getWinesByQuery(SearchWinesParcel $parcel) {
		
		lastUnqualified.clear();
		lastQualified.clear();
		
		String svcUrl = _API_URL 
				+ _SERVICE_WINES + "?akey=" + _API_KEY  
				+ "&f=" + ((($parcel.page-1)*SearchData.API_WINE_RESULTS)+1)
				+ "&n=" + SearchData.API_WINE_RESULTS;
		if( !$parcel.ip.equals("") )
			svcUrl += "&ip=" + $parcel.ip;
		if( !$parcel.zip.equals("") )
			svcUrl += "&z=" + $parcel.zip;
		if( !$parcel.country.equals("") )
			svcUrl += "&c=" + $parcel.country;			
		if( $parcel.value>0 )
			svcUrl += "&xp=" + $parcel.value;
		if( !$parcel.getQuery().equals("") )
			svcUrl += "&q=" + URLEncoder.encode( $parcel.getQuery() );
		//Log.d(TAG, svcUrl);
		execute( new AsyncServiceParcel[]{ getSearchInstance(svcUrl, $parcel.getQuery(), _SERVICE_WINES) } );
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
			HttpResponse resp;
			try{
				resp = httpClient.execute( httpReq );
				resultPrcl.result  = EntityUtils.toString( resp.getEntity() );
				
			}catch( IOException $e ){
				$e.printStackTrace();
				resultPrcl.result = "failed";
			}
		}

		return resultPrcl;
	}
	
	@Override
	protected void onPostExecute(AsyncServiceParcel $parcel) {
		WineParcel wine;

		try{
			JSONObject resultObj = new JSONObject( $parcel.result );
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_META);
			if( statusObj.getInt(_FIELD_RETURNED)>0 ){
				JSONArray listAry = resultObj.getJSONArray(_FIELD_WINES);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createSnoothWine( listAry.getJSONObject(a) );
					if( WineFactory.isQualified( wine, $parcel.query ) )
						lastQualified.add( wine );
					else
						lastUnqualified.add( wine );
				}
			}else{
				//Log.d(TAG, "getWinesByQuery No Results found" );
			}
			
		}catch( JSONException $e ){
			//Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
		}
		
		_context.resume( $parcel );
	}
	
	private AsyncServiceParcel getSearchInstance( String $url, String $query, String $svc ){
		AsyncServiceParcel parcel = new AsyncServiceParcel();
		parcel.query = $query;
		parcel.url = $url;
		parcel.svc = $svc;
		parcel.app_id = API_ID;
		return parcel;
	}

}
