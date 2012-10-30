package com.yno.wizard.model.fb;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.Util;

public class FbModelFactory {
	
	public static final String TAG = FbModelFactory.class.getSimpleName();

	public static FbUserParcel createUser( String $json ){
		FbUserParcel user = new FbUserParcel();
		
		try{
			JSONObject obj = new JSONObject($json);
			
			try{
				user.birthday = obj.getString("birthday");
				Log.d(TAG, user.birthday);
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.first_name = obj.getString("first_name");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.gender = obj.getString("gender");
				Log.d(TAG, user.gender);
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.id = obj.getString("id");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.last_name = obj.getString("last_name");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.link = obj.getString("link");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.location = obj.getString("location");
				Log.d(TAG, user.location);
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.locale = obj.getString("locale");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.name = obj.getString("name");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.picture = obj.getString("picture");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
			try{
				user.username = obj.getString("username");
			}catch(JSONException $ee){
				$ee.printStackTrace();
			}
			
		}catch(JSONException $e){
			$e.printStackTrace();
		}
		
		return user;
	}
}
