package com.ynotasting.finder.model.service;

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

import android.util.Log;

import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.WineFactory;
import com.ynotasting.finder.model.WineParcel;

public class GoogleServiceClass implements IWineSearchService {
	
	public static final String TAG = GoogleServiceClass.class.getSimpleName();
	public static final String API_ID = "google";
	
	private static final String _API_URL = "https://www.googleapis.com";
	private static final String _API_KEY = "AIzaSyB9XTrRYvetXGByIjaMI60Ru2wHzj_mWW8";
	private static final String _SERVICE_PRODUCTS = "/shopping/search/v1/public/products";
	private static final String _FIELD_TOTAL = "totalItems";
	private static final String _FIELD_ITEMS = "items";
	
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
		String json = getSearchInstance( _SERVICE_PRODUCTS, URLEncoder.encode( $parcel.getQuery() ), $parcel.page, SearchData.API_WINE_RESULTS );
		
		try{
			JSONObject resultObj = new JSONObject( json );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				int a,l = 0;
				for( a=0, l=itemsAry.length(); a<l; a++){
					wine = WineFactory.createGoogleWine( itemsAry.getJSONObject(a) );
					wines.add( wine );
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						_lastQualified.add( wine );
					else
						_lastUnqualified.add( wine );
				}
			}else{
				Log.e(TAG, "getWinesByQuery No results found");
			}
			
		}catch( JSONException $e ){
			Log.e(TAG, "getWinesByQuery unable to parse JSON: " + $e.toString() );
		}
		return wines;
	}
	
	public WineParcel getWineByBarcode( String $barcode ){
		
		WineParcel wine = new WineParcel();
		
		String json = getSearchInstance( _SERVICE_PRODUCTS, $barcode, 1, 1);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				wine = WineFactory.createGoogleWine( itemsAry.getJSONObject(0) );
				wine = getWineDetails( wine );
			}else{
				Log.e(TAG, "getWineByBarcode No results found");
			}
			
		}catch( JSONException $e ){
			Log.e(TAG, "getWineByBarcode unable to parse JSON: " + $e.toString() );
		}
		
		return wine;
	}
	
	public WineParcel getWineDetails( WineParcel $wine ){
		
		String json = getSearchInstance( _SERVICE_PRODUCTS, URLEncoder.encode( $wine.nameQualified ), 1, SearchData.API_WINE_RESULTS );
		WineParcel finalWine = $wine;
		WineParcel compWine = null;
		
		try{
			JSONObject resultObj = new JSONObject( json );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				for( int a=0, l=itemsAry.length(); a<l; a++){
					compWine = WineFactory.createGoogleWine( itemsAry.getJSONObject(a) );				
					if( WineFactory.compareWines(compWine, finalWine) )
						finalWine = WineFactory.combineWines( finalWine, compWine );
				}
			}else{
				Log.e(TAG, "getWineDetails No results found");
			}
		}catch( JSONException $e ){
			Log.e(TAG, "getWineDetails unable to parse JSON: " + $e.toString() );
		}
		
		return finalWine;
	}
	
	private String getSearchInstance( String $svc, String $query, int $page, int $results ){
		String json = "";
		String svcUrl = _API_URL + $svc
				+ "?key=" + _API_KEY 
				+ "&q=" + $query
				+ "&country=US"
				+ "&rankBy=relevancy";
		//Log.d(TAG, svcUrl);
		HttpClient httpClient = SearchData.getHttpClient();
		HttpRequestBase httpReq = new HttpGet( svcUrl );
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
