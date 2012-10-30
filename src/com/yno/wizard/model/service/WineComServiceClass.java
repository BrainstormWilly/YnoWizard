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

import android.util.Log;

import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

public class WineComServiceClass implements IWineSearchService {
	
	public static final String TAG = WineComServiceClass.class.getSimpleName();
	public static final String API_ID = "winecom";
	
	private static final String _API_URL = "http://services.wine.com/api/beta2/service.svc/json/";
	private static final String _API_KEY = "dfdd2a54c70773fac5209329860c5fa1";
	private static final String _SERVICE_CATALOG = "catalog";
	private static final String _SERVICE_CATS = "categorymap";
	private static final String _FIELD_STATUS = "Status";
	private static final String _FIELD_RETURNCODE = "ReturnCode";
	private static final String _FIELD_PRODUCTS = "Products";
	private static final String _FIELD_RESULTS = "List";
	
	private ArrayList<WineParcel> _lastUnqualified = new ArrayList<WineParcel>();
	private ArrayList<WineParcel> _lastQualified = new ArrayList<WineParcel>();
	
	public ArrayList<WineParcel> getLastUnqualifiedWines(){
		return _lastUnqualified;
	}
	
	public ArrayList<WineParcel> getLastQualifiedWines(){
		return _lastQualified;
	}
	
	public ArrayList<WineParcel> getWinesByQuery( SearchWinesParcel $parcel ){
		WineParcel wine;
		ArrayList<WineParcel> wines = new ArrayList<WineParcel>();
		String filter = "&filter=";
		
		_lastUnqualified.clear();
		_lastQualified.clear();

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
		
		Log.d(TAG, url);
		String json = getSearchInstance( url );
		
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONObject statusObj = resultObj.getJSONObject(_FIELD_STATUS);
			if( statusObj.getInt(_FIELD_RETURNCODE)==0 ){
				JSONObject productObj = resultObj.getJSONObject(_FIELD_PRODUCTS);
				JSONArray listAry = productObj.getJSONArray(_FIELD_RESULTS);
				for( int a=0, l= listAry.length(); a<l; a++ ){
					wine = WineFactory.createWineComWine( listAry.getJSONObject(a) );
					wines.add( wine );
					if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
						_lastQualified.add( wine );
					else
						_lastUnqualified.add( wine );
				}
			}else{
				Log.d(TAG, "getWinesByQuery Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
			}
			
		}catch( JSONException $e ){
			Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
		}
		
		return wines;
	}
	
	public ArrayList<SearchTypeParcel> getWineTypes(){
		int a,b,al,bl;
		SearchTypeParcel parcel;
		ArrayList<SearchTypeParcel> typeCats = new ArrayList<SearchTypeParcel>();
		ArrayList<SearchTypeParcel> varCats = new ArrayList<SearchTypeParcel>();
		ArrayList<SearchTypeParcel> allCats = new ArrayList<SearchTypeParcel>();
		
		String url = _API_URL 
				+ _SERVICE_CATS + "?apikey=" + _API_KEY 
				+ "&filter=categories(490)";
		String json = getSearchInstance( url );
		
		try{
			JSONObject itemObj;
			JSONObject resultObj = new JSONObject(json);
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
							typeCats.add(parcel);
						}
						//Collections.sort(typeCats);
					}
					if(itemObj.getString("Name").equals("Varietal")){
						subCatsAry = itemObj.getJSONArray("Refinements");
						for( b=0, bl=subCatsAry.length(); b<bl; b++ ){
							parcel = new SearchTypeParcel();
							parcel.setName( subCatsAry.getJSONObject(b).getString("Name") );
							parcel.id = subCatsAry.getJSONObject(b).getInt("Id");
							varCats.add(parcel);
						}
						//Collections.sort(varCats);
					}
				}
				allCats.addAll(typeCats);
				allCats.addAll(varCats);
			}else{
				Log.d(TAG, "getWineTypes Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "getWineTypes Unable to parse JSON result" );
		}
		
		
		return allCats;
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
