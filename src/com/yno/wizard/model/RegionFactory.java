package com.yno.wizard.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.yno.wizard.model.service.VinTankServiceClass;

public class RegionFactory {
	
	public static RegionParcel createVinTankRegion( JSONObject $obj ){
		RegionParcel reg = new RegionParcel();
		
		reg.api = VinTankServiceClass.API_ID;
		
		try{
			reg.id = $obj.getString("ynId");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		try{
			reg.name = $obj.getString("name");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}

		try{
			reg.parent = $obj.getJSONObject("parent").getString("name");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}
		
		return reg;
	}
	
	public static String createVinTankRegionName( JSONObject $obj ){
		try{
			return $obj.getString("name");
		}catch( JSONException $e ){
			$e.printStackTrace();
		}

		return "";
	}

}
