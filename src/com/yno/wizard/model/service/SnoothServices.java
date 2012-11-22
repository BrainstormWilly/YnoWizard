package com.yno.wizard.model.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

public class SnoothServices implements IWineSearchService {
	
	public static final String TAG = SnoothServices.class.getSimpleName();
	public static final String API_ID = "snooth";
	
	private static final String _API_URL = "http://api.snooth.com/";
	private static final String _API_KEY = "fcgnjdqjj7116z3uakkpgr20wl24mwwuiwux67bov1bnyvzo";
	private static final String _SERVICE_WINES = "wines/";
	private static final String _FIELD_META = "meta";
	private static final String _FIELD_RETURNED = "returned";
	private static final String _FIELD_WINES = "wines";
	

	@Override
	public WinesServiceParcel getWinesByQuery( SearchWinesParcel $parcel ) {
		WineParcel wine;
		
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
		WinesServiceParcel parcel = (WinesServiceParcel) getSearchInstance(SearchData.ID_SERVICE_WINES, _SERVICE_WINES, svcUrl, $parcel.getQuery(), 1, SearchData.API_WINE_RESULTS);
		parcel.result = doService(parcel);
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_META);
			if( statusObj.getInt(_FIELD_RETURNED)>0 ){
				JSONArray listAry = resultObj.getJSONArray(_FIELD_WINES);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createSnoothWine( listAry.getJSONObject(a) );
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						parcel.qualified.add( wine );
					else
						parcel.unqualified.add( wine );
				}
			}else{
				//Log.d(TAG, "getWinesByQuery No Results found" );
			}
			
		}catch( JSONException $e ){
			//Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
		}
		
		return parcel;
	}
	
	private String doService( AsyncServiceParcel $parcel ){
		String result = "failed";
		final AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		final HttpRequestBase httpReq = new HttpGet( $parcel.url );
		
		try{
			HttpResponse resp = httpClient.execute( httpReq );
			final int status = resp.getStatusLine().getStatusCode();
			if( status!=HttpStatus.SC_OK )
				return result;
			final HttpEntity entity = resp.getEntity();
			if( entity!=null)
				result = EntityUtils.toString( resp.getEntity() );
		}catch( IOException $e ){
			httpReq.abort();
			$e.printStackTrace();
		}finally{
			if( httpClient!=null )
				httpClient.close();
		}
		return result;
	}
	
	private AsyncServiceParcel getSearchInstance( int $svcId, String $svc, String $url, String $query, int $page, int $results ){
		AsyncServiceParcel parcel;
		if( $svcId==SearchData.ID_SERVICE_WINES ){
			parcel = new WinesServiceParcel();
		}else{
			parcel = new AsyncServiceParcel();
		}
		parcel.app_id = API_ID;
		parcel.svc_id = $svcId;
		parcel.svc = $svc;
		parcel.url = $url;
		parcel.query = $query;
		parcel.page = $page;
		return parcel;
	}
	

}
