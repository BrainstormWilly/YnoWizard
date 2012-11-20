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

import android.os.AsyncTask;
import android.util.Log;

import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.TextSearchActivity;
import com.yno.wizard.view.TextSearchServiceHelper;

public class WineComAsyncService extends AsyncTask<AsyncServiceParcel, Void, AsyncServiceParcel> {
	
	public static final String TAG = "WineComAsyncService";
	public static final String API_ID = "winecom";
	
	private static final String _API_URL = "http://services.wine.com/api/beta2/service.svc/json/";
	private static final String _API_KEY = "dfdd2a54c70773fac5209329860c5fa1";
	private static final String _SERVICE_CATALOG = "catalog";
	private static final String _SERVICE_CATS = "categorymap";
	private static final String _FIELD_STATUS = "Status";
	private static final String _FIELD_RETURNCODE = "ReturnCode";
	private static final String _FIELD_PRODUCTS = "Products";
	private static final String _FIELD_RESULTS = "List";
	
	private IServiceContext _context;
	public ArrayList<SearchTypeParcel> types = new ArrayList<SearchTypeParcel>();
	public ArrayList<WineParcel> lastUnqualified = new ArrayList<WineParcel>();
	public ArrayList<WineParcel> lastQualified = new ArrayList<WineParcel>();
	
	public WineComAsyncService( IServiceContext $ctx ){
		_context = $ctx;
	}



	//@Override
	public void getWinesByQuery(SearchWinesParcel $parcel) {
		
		String filter = "&filter=";
		
		lastUnqualified.clear();
		lastQualified.clear();

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
		
		execute( new AsyncServiceParcel[]{ getSearchInstance( _SERVICE_CATALOG, $parcel.getQuery(), url ) } );
		
	}
	
	public void getWineTypes(){
		types.clear();
		
		String url = _API_URL 
				+ _SERVICE_CATS + "?apikey=" + _API_KEY 
				+ "&filter=categories(490)";
		
		execute( new AsyncServiceParcel[]{ getSearchInstance( _SERVICE_CATS, "", url ) } );
	}
	
	
	
	@Override
	protected AsyncServiceParcel doInBackground(AsyncServiceParcel... $parcels) {
		
		AsyncServiceParcel resultPrcl = new AsyncServiceParcel();
		
		for (AsyncServiceParcel parcel : $parcels){
			resultPrcl.query = parcel.query;
			resultPrcl.svc = parcel.svc;
			resultPrcl.url = parcel.url;
			
			if( parcel.svc.equals(_SERVICE_CATALOG) ){
				try{
					HttpClient httpClient = SearchData.getHttpClient();
					HttpRequestBase httpReq = new HttpGet( parcel.url );
					HttpResponse resp;
					resp = httpClient.execute( httpReq );
					resultPrcl.result = EntityUtils.toString( resp.getEntity() );
					
				}catch( IOException $e ){
					$e.printStackTrace();
					resultPrcl.result = "failed";
				}
			}else{
				
			}
		}

		return resultPrcl;
	}
	
	@Override
	protected void onPostExecute(AsyncServiceParcel $parcel) {
		
		//Log.d(TAG, "onPostExecute");
		if( $parcel.svc.equals(_SERVICE_CATALOG) ){
			try{
				WineParcel wine;
				JSONObject resultObj = new JSONObject( $parcel.result );
				JSONObject statusObj = resultObj.getJSONObject(_FIELD_STATUS);
				if( statusObj.getInt(_FIELD_RETURNCODE)==0 ){
					JSONObject productObj = resultObj.getJSONObject(_FIELD_PRODUCTS);
					JSONArray listAry = productObj.getJSONArray(_FIELD_RESULTS);
					for( int a=0, l= listAry.length(); a<l; a++ ){
						wine = WineFactory.createWineComWine( listAry.getJSONObject(a) );
						if( WineFactory.isQualified( wine, $parcel.query ) )
							lastQualified.add( wine );
						else
							lastUnqualified.add( wine );
					}
				}else{
					//Log.d(TAG, "getWinesByQuery Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
				}
				
			}catch( JSONException $e ){
				//Log.d(TAG, "getWinesByQuery Unable to parse JSON result " + $parcel.getQuery() );
			}
		}else{
			try{
				int a,b,al,bl;
				SearchTypeParcel parcel;
				ArrayList<SearchTypeParcel> typeCats = new ArrayList<SearchTypeParcel>();
				ArrayList<SearchTypeParcel> varCats = new ArrayList<SearchTypeParcel>();
				//ArrayList<SearchTypeParcel> allCats = new ArrayList<SearchTypeParcel>();
				
				JSONObject itemObj;
				JSONObject resultObj = new JSONObject( $parcel.result );
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
					types.addAll(typeCats);
					types.addAll(varCats);
				}else{
					//Log.d(TAG, "getWineTypes Bad Return Code: " + statusObj.getInt(_FIELD_RETURNCODE) );
				}
			}catch( JSONException $e ){
				//Log.d(TAG, "getWineTypes Unable to parse JSON result" );
			}
		}	
		
		_context.resume($parcel);
		
	}
	
	private AsyncServiceParcel getSearchInstance( String $svc, String $query, String $url ){
		AsyncServiceParcel parcel = new AsyncServiceParcel();
		parcel.app_id = API_ID;
		parcel.svc = $svc;
		parcel.query = $query;
		parcel.url = $url;
		return parcel;
		
	}

}
