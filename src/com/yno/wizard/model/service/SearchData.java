package com.yno.wizard.model.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class SearchData {

//	public static final String GOOGLE_API_ID = "google";
//	public static final String SNOOTH_API_ID = "snooth";
//	public static final String VINTANK_API_ID = "vintank";
//	public static final String WINECOM_API_ID = "winecom";
	
	public static String[] APIS = { WineComServices.API_ID, VinTankServices.API_ID, SnoothServices.API_ID };
	public static int API_WINE_RESULTS = 15;
	public static int API_CATEGORY_RESULTS = 50;
	public static int API_TIMEOUT = 8000;
	
	public static int AUTOCOMPLETE_TOTAL = 6;
	
	public static final String SEARCH_TYPE_PHRASE = "phrase";
	public static final String SEARCH_TYPE_BARCODE = "barcode";
	public static final String SEARCH_TYPE_MANUAL = "manual";
	public static final String SEARCH_TYPE = "type";
	
	public static final int ID_SERVICE_WINES = 1;
	public static final int ID_SERVICE_PHRASE_AUTOCOMPLETE = 2;
	public static final int ID_SERVICE_WINE_TYPES = 3;
	public static final int ID_SERVICE_WINE_DETAILS_BARCODE = 4;
	public static final int ID_SERVICE_WINE_DETAILS_PHRASE = 5;
	
	public static final String QUERY = "query";
	public static final String QUERY_VARIETALS = "queryVarietals";
	public static final String QUERY_WINES_PHRASE = "queryWinesPhrase";
	public static final String QUERY_WINE_BARCODE = "queryWineBarcode";
	public static final String QUERY_WINE_NAME = "queryWineName";
	
	
	public static HttpClient getHttpClient(){
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, API_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, API_TIMEOUT);
		return new DefaultHttpClient(params);
	}
}
