package com.ynotasting.finder.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ynotasting.finder.model.service.VinTankServiceClass;

public class ProducerFactory {
	
	public final static String TAG = ProducerFactory.class.getSimpleName();
	
	private static String REGEX_WINERY = ".*[wW]inery.*";
	private static String REGEX_VINEYARD = ".*[vV]inyards?.*";
	private static String REGEX_ESTATE = ".*[eE]states?.*";
	private static String REGEX_W_AND_V = ".*[wW]inery\\s((and)|&)\\s[vV]ineyards?.*";
	private static String REGEX_V_AND_W = ".*[vV]ineyards?\\s((and)|&)\\s[wW]inery.*";
	private static String REGEX_WINERYR = "\\s?[wW]inery";
	private static String REGEX_VINEYARDR = "\\s?[vV]ineyards?";
	private static String REGEX_ESTATER = "\\s?[eE]states?";
	private static String REGEX_W_AND_VR = "\\s[wW]inery\\s((and)|&)\\s[vV]ineyards?";
	private static String REGEX_V_AND_WR = "\\s[vV]ineyards?\\s((and)|&)\\s[wW]inery";
	
	public static String getProducerBase( String $phrase ){
		if( $phrase.equals("") )
			return "";
		if( $phrase.matches(REGEX_W_AND_V) )
			return $phrase.replaceAll(REGEX_W_AND_VR, "");
		else if( $phrase.matches(REGEX_V_AND_W) )
			return $phrase.replaceAll(REGEX_V_AND_WR, "");
		else if( $phrase.matches(REGEX_WINERY) ){
			return $phrase.replaceAll(REGEX_WINERYR, "");
		}else if( $phrase.matches(REGEX_VINEYARD) )
			return $phrase.replaceAll(REGEX_VINEYARDR, "");
		else if( $phrase.matches(REGEX_ESTATE) )
			return $phrase.replaceAll(REGEX_ESTATER, "");
		
		return $phrase;
	}

	public static ProducerParcel createVinTankProducer( JSONObject $obj ){
		ProducerParcel prod = new ProducerParcel();
		
		prod.api = VinTankServiceClass.API_ID;
		
		try{
			prod.id = $obj.getString("ynId");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		try{
			prod.name = $obj.getString("name");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		try{
			prod.email = $obj.getString("infoEmailAddress");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		try{
			prod.website = $obj.getString("homeURL");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		return prod;
	}
	
	public static String createVinTankProducerName( JSONObject $obj ){

		try{
			return $obj.getString("name");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		return "";
	}
}
