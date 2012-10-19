package com.ynotasting.finder.model;

import org.json.JSONException;
import org.json.JSONObject;

public class VarietalFactory {

	public static VarietalParcel createVinTankVarietal( JSONObject $data ){
		VarietalParcel var = new VarietalParcel();
		try{
			var.var_name = $data.getString("defaultName");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		try{
			var.var_type = $data.getString("wineType");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		return var;
		
	}
	
	public static String createVinTankVarietalName( JSONObject $data ){

		try{
			return $data.getString("defaultName");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		return "";
		
	}
}
