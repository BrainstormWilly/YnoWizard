package com.yno.wizard.model.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

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

import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.WineTypesComparator;

public class WineComServices implements IWineSearchService {
	
	public static final String TAG = WineComServices.class.getSimpleName();
	public static final String API_ID = "winecom";
	
	private static final String _API_URL = "http://services.wine.com/api/beta2/service.svc/json/";
	private static final String _API_KEY = "dfdd2a54c70773fac5209329860c5fa1";
	private static final String _SERVICE_CATALOG = "catalog";
	private static final String _SERVICE_CATS = "categorymap";
	private static final String _FIELD_STATUS = "Status";
	private static final String _FIELD_RETURNCODE = "ReturnCode";
	private static final String _FIELD_PRODUCTS = "Products";
	private static final String _FIELD_RESULTS = "List";
	
	
	public WinesServiceParcel getWinesByQuery( SearchWinesParcel $parcel ){
		WineParcel wine;
		String filter = "&filter=";

		String url = _API_URL 
				+ _SERVICE_CATALOG + "?apikey=" + _API_KEY 
				+ "&offset=" + ((($parcel.page-1)*SearchData.API_WINE_RESULTS)+1)
				+ "&size=" + SearchData.API_WINE_RESULTS
				+ "&sort=popularity";
		if( !$parcel.state.equals("") )
			url += "&state=" + $parcel.state;
		if( $parcel.type!=null && $parcel.type.id>0 ){
			filter +=  URLEncoder.encode("categories(490+" + $parcel.type.id + ")");
		}else{
			filter = URLEncoder.encode("categories(490)");
		}
		if( $parcel.value>0 )
			filter += "+";
			filter += URLEncoder.encode("price(0|" + $parcel.value + ")");
		url += filter;
		
		if( !$parcel.name.equals("") )
			url += "&search=" + URLEncoder.encode( $parcel.name );
		
		//Log.d(TAG, url);
		WinesServiceParcel parcel = (WinesServiceParcel) getSearchInstance(SearchData.ID_SERVICE_WINES, _SERVICE_CATALOG, url, $parcel.getQuery(), 1, SearchData.API_WINE_RESULTS);
		parcel.result = doService(parcel);
		
		try{
			JSONObject resultObj = new JSONObject( parcel.result );
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_STATUS);
			if( statusObj.getInt(_FIELD_RETURNCODE)==0 ){
				JSONObject productObj = resultObj.getJSONObject(_FIELD_PRODUCTS);
				JSONArray listAry = productObj.getJSONArray(_FIELD_RESULTS);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createWineComWine( listAry.getJSONObject(a) );
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						parcel.qualified.add(wine);
					else
						parcel.unqualified.add(wine);
				}
			}else{
				//Log.d(TAG, "getWinesByQuery Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
			}	
		}catch( JSONException $e ){
			$e.printStackTrace();
			//Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
		}
		
		return parcel;
	}
	
	public WineTypesServiceParcel getWineTypes(){
		int a,b,al,bl;
		SearchTypeParcel parcel;
		
		String url = _API_URL 
				+ _SERVICE_CATS + "?apikey=" + _API_KEY 
				+ "&filter=categories(490)";
		
		WineTypesServiceParcel typesPrcl = (WineTypesServiceParcel) getSearchInstance(SearchData.ID_SERVICE_WINE_TYPES, _SERVICE_CATS, url, "", 1, SearchData.API_CATEGORY_RESULTS);
		typesPrcl.result = doService(typesPrcl);
		
		try{
			JSONObject itemObj;
			JSONObject resultObj = new JSONObject(typesPrcl.result);
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_STATUS);
			JSONArray catsAry;
			JSONArray subCatsAry;
			
			if( statusObj.getInt(_FIELD_RETURNCODE)==0 ){
				catsAry = resultObj.getJSONArray("Categories");
				for( a=0, al=catsAry.length(); a<al; a++ ){
					itemObj = catsAry.getJSONObject(a);
					if(itemObj.getString("Name").equals("Wine Type")){
						subCatsAry = itemObj.getJSONArray("Refinements");
						for( b=0, bl=subCatsAry.length(); b<bl; b++ ){
							parcel = new SearchTypeParcel();
							parcel.setName( subCatsAry.getJSONObject(b).getString("Name") );
							parcel.id = subCatsAry.getJSONObject(b).getInt("Id");
							typesPrcl.types.add(parcel);
						}
						//Collections.sort(typeCats);
					}
					if(itemObj.getString("Name").equals("Varietal")){
						subCatsAry = itemObj.getJSONArray("Refinements");
						for( b=0, bl=subCatsAry.length(); b<bl; b++ ){
							parcel = new SearchTypeParcel();
							parcel.setName( subCatsAry.getJSONObject(b).getString("Name") );
							parcel.id = subCatsAry.getJSONObject(b).getInt("Id");
							typesPrcl.types.add(parcel);
						}
						//Collections.sort(varCats);
					}
				}
			}else{
				//Log.d(TAG, "getWineTypes Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
			}
		}catch( JSONException $e ){
			//Log.d(TAG, "getWineTypes Unable to parse JSON result" );
		}
		
		Collections.sort(typesPrcl.types, new WineTypesComparator());
		
		return typesPrcl;
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
			parcel = new WineTypesServiceParcel();
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
