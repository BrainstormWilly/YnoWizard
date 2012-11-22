package com.yno.wizard.model.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

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
import android.util.Log;

import com.yno.wizard.model.ProducerFactory;
import com.yno.wizard.model.RegionFactory;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.VarietalFactory;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

public class VinTankServices  {
	
	public static final String TAG = "VinTankAsyncService";
	public static final String API_ID = "vintank";
	//public static final int SERVICE_VARIETALS_ID = 3;
	public static final String SERVICE_WINES = "/search/wines";
	public static final String SERVICE_PRODUCERS = "/search/brands";
	public static final String SERVICE_REGIONS = "/search/regions";
	public static final String SERVICE_VARIETALS = "/search/varieties";
	
	private static final String _API_URL = "http://apiv1.cruvee.com";
	private static final String _API_KEY = "898153ea1edb4e6eb9c44033670a5299";
	private static final String _API_SECRET = "3466c4e402a34b4da490de8c2f11ba49";
	private static final String _QUERY_RESULTS = "results";
	
	
	public WinesServiceParcel getWinesByQuery( SearchWinesParcel $parcel ){
		
		WineParcel wine;
		WinesServiceParcel parcel = (WinesServiceParcel) getSearchInstance( SearchData.ID_SERVICE_WINES, SERVICE_WINES, $parcel.getQuery() , $parcel.page, SearchData.API_WINE_RESULTS );
		parcel.result = doService( parcel );
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				wine = WineFactory.createVinTankWine( listAry.getJSONObject(a) );
				if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
					parcel.qualified.add( wine );
				else
					parcel.unqualified.add( wine );
			}
		}catch( JSONException $e ){
			//Log.d(TAG, "No wines found for query " + $parcel.getQuery() );
		}
		
		return parcel;
	}

	public PhraseServiceParcel getWineNamesByQuery( String $query, int $page, int $results ){
		
		PhraseServiceParcel parcel = (PhraseServiceParcel) getSearchInstance( SearchData.ID_SERVICE_PHRASE_AUTOCOMPLETE, SERVICE_WINES, $query , $page, $results );
		parcel.result = doService( parcel );
		if( !parcel.result.equals("failed") ){
			try{
				JSONObject resultObj = new JSONObject( parcel.result );
				JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
				for( int a=0, l= listAry.length(); a<l; a++ )
					parcel.values.add( WineFactory.createVinTankWineName(listAry.getJSONObject(a)) );
				
			}catch( JSONException $e ){
				//Log.d(TAG, "No wine names found for query " + $query );
			}
		}
		return parcel;			
	}
	
	public PhraseServiceParcel getProducerNamesByQuery( String $query, int $page, int $results ){
		
		PhraseServiceParcel parcel = (PhraseServiceParcel) getSearchInstance( SearchData.ID_SERVICE_PHRASE_AUTOCOMPLETE, SERVICE_PRODUCERS, $query , $page, $results );
		parcel.result = doService( parcel );
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ )
				parcel.values.add( ProducerFactory.createVinTankProducerName( listAry.getJSONObject(a) ) );
			
		}catch( JSONException $e ){
			//Log.d(TAG, "No wine names found for query " + $query );
		}
		return parcel;
	}
	
	public PhraseServiceParcel getRegionNamesByQuery( String $query, int $page, int $results ){
		
		PhraseServiceParcel parcel = (PhraseServiceParcel) getSearchInstance( SearchData.ID_SERVICE_PHRASE_AUTOCOMPLETE, SERVICE_REGIONS, $query, $page, $results);
		parcel.result = doService( parcel );
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ )
				parcel.values.add( RegionFactory.createVinTankRegionName( listAry.getJSONObject(a) ) );
			
		}catch( JSONException $e ){
			//Log.d(TAG, "No wine names found for query " + $query );
		}
		return parcel;
	}
	
	public PhraseServiceParcel getVarietalNamesByQuery( String $query, int $page, int $results ){
		
		PhraseServiceParcel parcel = (PhraseServiceParcel) getSearchInstance( SearchData.ID_SERVICE_PHRASE_AUTOCOMPLETE, SERVICE_VARIETALS, $query, $page, $results);
		parcel.result = doService( parcel );
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ )
				parcel.values.add( VarietalFactory.createVinTankVarietalName( listAry.getJSONObject(a) ) );
			
		}catch( JSONException $e ){
			//Log.d(TAG, "No wine names found for query " + $query );
		}
		return parcel;
	}
	
	
	
//	private void cancelThread( Thread $thread ){
//		try{
//			Thread temp = $thread;
//			$thread = null;
//			temp.interrupt();
//		}catch( Exception $e ){
//			// ignore
//		}
//	}
	
	private String doService( AsyncServiceParcel $parcel ){
		String result = "failed";
		final AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		final HttpRequestBase httpReq = new HttpGet( $parcel.url );
		
		try{
			httpReq.addHeader( "Authorization", getSignature( $parcel.svc ) );
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
	
	private AsyncServiceParcel getSearchInstance( int $svcId, String $svc, String $query, int $page, int $results ){
		AsyncServiceParcel parcel;
		String svcUrl = _API_URL 
				+ $svc + "?q=" + URLEncoder.encode( $query )
				+ "&page=" + $page
				+ "&rpp=" + $results
				+ "&fmt=json";
		if( $svcId==SearchData.ID_SERVICE_PHRASE_AUTOCOMPLETE ){
			parcel = new PhraseServiceParcel();
		}else{
			parcel = new WinesServiceParcel();
		}
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
