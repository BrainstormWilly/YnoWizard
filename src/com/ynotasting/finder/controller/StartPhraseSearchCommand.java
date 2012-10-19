package com.ynotasting.finder.controller;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Messenger;

import com.ynotasting.finder.model.LocationModel;
import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.service.SearchData;

public class StartPhraseSearchCommand {
	
	public final static String TAG = StartPhraseSearchCommand.class.getSimpleName();
	public static String ACTION = "com.ynotasting.finder.intent.START_PHRASE_SEARCH";
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	
	private Context _context;
	
	public StartPhraseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			SearchWinesParcel parcel = payload.getParcelable(SearchWinesParcel.NAME);
			LocationModel loc = new LocationModel( _context );
			parcel.ip = loc.getLocalIpAddress();
			Address primAddr = loc.getPrimaryAddress();
			if( primAddr!=null ){
				parcel.country = primAddr.getCountryCode();
				parcel.state = loc.getStateCode( primAddr.getAdminArea() );
				parcel.zip = primAddr.getPostalCode();
			}
			Intent intent = new Intent( ACTION );
			intent.putExtra(SearchWinesParcel.NAME, parcel);
			_context.startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}

}
