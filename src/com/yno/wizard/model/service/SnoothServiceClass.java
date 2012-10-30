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

import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import com.yno.wizard.YnoFinderApplication;
import com.yno.wizard.model.LocationModel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

public class SnoothServiceClass implements IWineSearchService {
	
	public static final String TAG = SnoothServiceClass.class.getSimpleName();
	public static final String API_ID = "snooth";
	
	private static final String _API_URL = "http://api.snooth.com/";
	private static final String _API_KEY = "fcgnjdqjj7116z3uakkpgr20wl24mwwuiwux67bov1bnyvzo";
	private static final String _SERVICE_WINES = "wines/";
	private static final String _FIELD_META = "meta";
	private static final String _FIELD_RETURNED = "returned";
	private static final String _FIELD_WINES = "wines";
	
	private ArrayList<WineParcel> _lastUnqualified = new ArrayList<WineParcel>();
	private ArrayList<WineParcel> _lastQualified = new ArrayList<WineParcel>();


	
	public ArrayList<WineParcel> getLastUnqualifiedWines(){
		return _lastUnqualified;
	}
	
	public ArrayList<WineParcel> getLastQualifiedWines(){
		return _lastQualified;
	}
	

	@Override
	public ArrayList<WineParcel> getWinesByQuery( SearchWinesParcel $parcel ) {
		WineParcel wine;
		ArrayList<WineParcel> wines = new ArrayList<WineParcel>();
		
		_lastUnqualified.clear();
		_lastQualified.clear();
		
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
		Log.d(TAG, svcUrl);
		String json = getSearchInstance( svcUrl );
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_META);
			if( statusObj.getInt(_FIELD_RETURNED)>0 ){
				JSONArray listAry = resultObj.getJSONArray(_FIELD_WINES);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createSnoothWine( listAry.getJSONObject(a) );
					wines.add(wine);
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						_lastQualified.add( wine );
					else
						_lastUnqualified.add( wine );
				}
			}else{
				Log.d(TAG, "getWinesByQuery No Results found" );
			}
			
		}catch( JSONException $e ){
			Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
		}
		
		return wines;
	}
	
	private String getSearchInstance( String $url ){
		String json = "";
		
		HttpClient httpClient = SearchData.getHttpClient();
		HttpRequestBase httpReq = new HttpGet( $url );
		HttpResponse resp;
		try{
			resp = httpClient.execute( httpReq );
			json  = EntityUtils.toString( resp.getEntity() );
		}catch( IOException $e ){
			$e.printStackTrace();
		}

		return json;
	}

}
