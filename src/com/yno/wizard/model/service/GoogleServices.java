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

public class GoogleServices implements IWineSearchService {
	
	public static final String TAG = GoogleServices.class.getSimpleName();
	public static final String API_ID = "google";
	
	private static final String _API_URL = "https://www.googleapis.com";
	private static final String _API_KEY = "AIzaSyB9XTrRYvetXGByIjaMI60Ru2wHzj_mWW8";
	private static final String _SERVICE_PRODUCTS = "/shopping/search/v1/public/products";
	private static final String _FIELD_TOTAL = "totalItems";
	private static final String _FIELD_ITEMS = "items";



	@Override
	public WinesServiceParcel getWinesByQuery( SearchWinesParcel $parcel ) {
		
		WineParcel wine;

		WinesServiceParcel parcel = (WinesServiceParcel) getSearchInstance( SearchData.ID_SERVICE_WINES, _SERVICE_PRODUCTS, $parcel.getQuery(), $parcel.page, SearchData.API_WINE_RESULTS );
		parcel.result = doService(parcel);
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				int a,l = 0;
				for( a=0, l=itemsAry.length(); a<l; a++){
					wine = WineFactory.createGoogleWine( itemsAry.getJSONObject(a) );
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						parcel.qualified.add( wine );
					else
						parcel.unqualified.add( wine );
				}
			}else{
				//.e(TAG, "getWinesByQuery No results found");
			}
			
		}catch( JSONException $e ){
			//Log.e(TAG, "getWinesByQuery unable to parse JSON: " + $e.toString() );
		}
		return parcel;
	}
	
	public WineDetailsServiceParcel getWineByBarcode( String $barcode ){
		
		WineDetailsServiceParcel parcel = (WineDetailsServiceParcel) getSearchInstance(SearchData.ID_SERVICE_WINE_DETAILS_BARCODE, _SERVICE_PRODUCTS, $barcode, 1, 1);
		parcel.isFromBarcode = 1;
		parcel.wine = new WineParcel();
		parcel.result = doService(parcel);
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				parcel.wine = WineFactory.createGoogleWine( itemsAry.getJSONObject(0) );
				parcel.wine = getWineDetails( parcel.wine ).wine;
			}else{
				//.e(TAG, "getWineByBarcode No results found");
			}
			
		}catch( JSONException $e ){
			//Log.e(TAG, "getWineByBarcode unable to parse JSON: " + $e.toString() );
		}
		
		
		return parcel;
	}
	
	public WineDetailsServiceParcel getWineDetails( WineParcel $wine ){
		
		WineParcel compWine;
		WineDetailsServiceParcel parcel = (WineDetailsServiceParcel) getSearchInstance(SearchData.ID_SERVICE_WINE_DETAILS_PHRASE, _SERVICE_PRODUCTS, $wine.nameQualified, 1, 1);
		parcel.isFromBarcode = 0;
		parcel.wine = $wine;
		parcel.result = doService(parcel);
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			
			if( resultObj.getInt(_FIELD_TOTAL)>0 ){
				JSONArray itemsAry = resultObj.getJSONArray(_FIELD_ITEMS);
				for( int a=0, l=itemsAry.length(); a<l; a++){
					compWine = WineFactory.createGoogleWine( itemsAry.getJSONObject(a) );				
					if( WineFactory.compareWines(compWine, parcel.wine) )
						parcel.wine = WineFactory.combineWines( parcel.wine, compWine );
				}
			}else{
				//Log.e(TAG, "getWineDetails No results found");
			}
		}catch( JSONException $e ){
			//Log.e(TAG, "getWineDetails unable to parse JSON: " + $e.toString() );
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
	
	private AsyncServiceParcel getSearchInstance( int $svcId, String $svc, String $query, int $page, int $results ){
		AsyncServiceParcel parcel;
		String svcUrl = _API_URL + $svc
				+ "?key=" + _API_KEY 
				+ "&q=" + URLEncoder.encode( $query )
				+ "&country=US"
				+ "&rankBy=relevancy";
		if( $svcId==SearchData.ID_SERVICE_WINES ){
			parcel = new WinesServiceParcel();
		}else{
			parcel = new WineDetailsServiceParcel();
		}
		parcel.app_id = API_ID;
		parcel.svc_id = $svcId;
		parcel.svc = $svc;
		parcel.url = svcUrl;
		parcel.query = $query;
		parcel.page = $page;
		return parcel;
	}

}
