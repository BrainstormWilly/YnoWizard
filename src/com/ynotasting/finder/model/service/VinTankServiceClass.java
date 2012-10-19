package com.ynotasting.finder.model.service;

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

import com.ynotasting.finder.model.ProducerFactory;
import com.ynotasting.finder.model.ProducerParcel;
import com.ynotasting.finder.model.RegionFactory;
import com.ynotasting.finder.model.RegionParcel;
import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.VarietalFactory;
import com.ynotasting.finder.model.VarietalParcel;
import com.ynotasting.finder.model.WineFactory;
import com.ynotasting.finder.model.WineParcel;

import android.util.Log;

public class VinTankServiceClass implements IWineSearchService {
	
	public static final String TAG = VinTankServiceClass.class.getSimpleName();
	public static final String API_ID = "vintank";
	
	private static final String _API_URL = "http://apiv1.cruvee.com";
	private static final String _API_KEY = "898153ea1edb4e6eb9c44033670a5299";
	private static final String _API_SECRET = "3466c4e402a34b4da490de8c2f11ba49";
	private static final String _SERVICE_PRODUCERS = "/search/brands";
	private static final String _SERVICE_REGIONS = "/search/regions";
	private static final String _SERVICE_WINES = "/search/wines";
	private static final String _SERVICE_VARIETALS = "/search/varieties";
	
	private static final String _QUERY_RESULTS_TOTAL = "total";
	private static final String _QUERY_RESULTS = "results";
	private static final String _QUERY_PAGE = "page";
	private static final String _QUERY_RESULTS_PAGE = "rpp";
	private static final String _FIELD_VAR_NAME = "defaultName";
	private static final String _FIELD_VAR_ID = "ynId";
	private static final String _FIELD_VAR_TYPE = "wineType";
	
	private ArrayList<WineParcel> _lastUnqualified = new ArrayList<WineParcel>();
	private ArrayList<WineParcel> _lastQualified = new ArrayList<WineParcel>();
	
	public ArrayList<WineParcel> getLastUnqualifiedWines(){
		return _lastUnqualified;
	}
	
	public ArrayList<WineParcel> getLastQualifiedWines(){
		return _lastQualified;
	}
	
	
	public ArrayList<VarietalParcel> getVarietalsByQuery( String $query, int $page, int $results ){
		ArrayList<VarietalParcel> vars = new ArrayList<VarietalParcel>();
		
		String json = getSearchInstance( _SERVICE_VARIETALS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				vars.add( VarietalFactory.createVinTankVarietal( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No varietals found for query " + $query );
		}
		
		return vars;
	}
	
	public ArrayList<String> getVarietalNamesByQuery( String $query, int $page, int $results ){
		ArrayList<String> vars = new ArrayList<String>();
		
		String json = getSearchInstance( _SERVICE_VARIETALS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				vars.add( VarietalFactory.createVinTankVarietalName( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No varietal names found for query " + $query );
		}
		
		return vars;
	}
	
	public ArrayList<WineParcel> getWinesByQuery( SearchWinesParcel $parcel ){
		WineParcel wine;
		ArrayList<WineParcel> winesAll = new ArrayList<WineParcel>();
		
		_lastUnqualified.clear();
		_lastQualified.clear();
		
		String json = getSearchInstance( _SERVICE_WINES, "?q="+URLEncoder.encode( $parcel.getQuery() ), $parcel.page, SearchData.API_WINE_RESULTS);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				wine = WineFactory.createVinTankWine( listAry.getJSONObject(a) );
				winesAll.add( wine );
				if( WineFactory.isQualified( wine, $parcel.getQuery() ) )
					_lastQualified.add( wine );
				else
					_lastUnqualified.add( wine );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No wines found for query " + $parcel.getQuery() );
		}
		
		return winesAll;
	}
	
	public ArrayList<String> getWineNamesByQuery( String $query, int $page, int $results ){
		ArrayList<String> winesAll = new ArrayList<String>();
		
		_lastUnqualified.clear();
		_lastQualified.clear();
		
		String json = getSearchInstance( _SERVICE_WINES, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ )
				winesAll.add( WineFactory.createVinTankWineName(listAry.getJSONObject(a)) );
			
		}catch( JSONException $e ){
			Log.d(TAG, "No wine names found for query " + $query );
		}
		
		return winesAll;
	}
	
	public ArrayList<ProducerParcel> getProducersByQuery( String $query, int $page, int $results ){
		ArrayList<ProducerParcel> prods = new ArrayList<ProducerParcel>();
		
		String json = getSearchInstance( _SERVICE_PRODUCERS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				prods.add( ProducerFactory.createVinTankProducer( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No producers found for query " + $query );
		}
		
		return prods;
	}
	
	public ArrayList<String> getProducerNamesByQuery( String $query, int $page, int $results ){
		ArrayList<String> prods = new ArrayList<String>();
		
		String json = getSearchInstance( _SERVICE_PRODUCERS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				prods.add( ProducerFactory.createVinTankProducerName( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No producer names found for query " + $query );
		}
		
		return prods;
	}
	
	public ArrayList<RegionParcel> getRegionsByQuery( String $query, int $page, int $results ){
		ArrayList<RegionParcel> regs = new ArrayList<RegionParcel>();
		
		String json = getSearchInstance( _SERVICE_REGIONS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				regs.add( RegionFactory.createVinTankRegion( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No regions found for query " + $query );
		}
		
		return regs;
	}
	
	public ArrayList<String> getRegionNamesByQuery( String $query, int $page, int $results ){
		ArrayList<String> regs = new ArrayList<String>();
		
		String json = getSearchInstance( _SERVICE_REGIONS, "?q="+URLEncoder.encode( $query ), $page, $results);
		
		try{
			JSONObject resultObj = new JSONObject( json );
			JSONArray listAry = resultObj.getJSONArray(_QUERY_RESULTS);
			for( int a=0, l= listAry.length(); a<l; a++ ){
				regs.add( RegionFactory.createVinTankRegionName( listAry.getJSONObject(a) ) );
			}
		}catch( JSONException $e ){
			Log.d(TAG, "No region names found for query " + $query );
		}
		
		return regs;
	}

	private String getSearchInstance( String $svc, String $query, int $page, int $results ){
		String json = "";
		Date now = new Date();
		String ms = String.valueOf( now.getTime() );
		String sig = getSignature( $svc, ms );
		String svcUrl = _API_URL 
				+ $svc + $query
				+ "&page=" + $page
				+ "&rpp=" + $results
				+ "&fmt=json";
		//Log.d(TAG, svcUrl);
		HttpClient httpClient = SearchData.getHttpClient();
		HttpRequestBase httpReq = new HttpGet( svcUrl );
		httpReq.addHeader("Authorization", "Cruvee appId=" + _API_KEY + ", sig=" + sig + ", timestamp=" + ms + ", uri=" + $svc);
		HttpResponse resp;
		try{
			resp = httpClient.execute( httpReq );
			json  = EntityUtils.toString( resp.getEntity() );
		}catch( IOException $e ){
			$e.printStackTrace();
		}

		return json;
	}
	
	private String getSignature( String $service, String $ms ){
		String hash = "";
		String auth =  _API_KEY + "\n";
		auth += "GET\n";
		auth += _API_SECRET + "\n";
		auth += $ms + "\n";
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
			Log.e(TAG, "doVinTankSearch unable to hash MD5: " + $e.toString() );
		}
		return hash;
	}
}


