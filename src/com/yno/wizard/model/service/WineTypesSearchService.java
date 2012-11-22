package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.SearchTypeParcel;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class WineTypesSearchService extends IntentService {

	public static final String TAG = WineTypesSearchService.class.getSimpleName();

	public WineTypesSearchService(){
		super( TAG );
	}
	
	@Override
	protected void onHandleIntent(Intent $intent) {
		Bundle extras = $intent.getExtras();
		Messenger msgr = (Messenger) extras.get("android.os.Messenger");
		
		WineComServices svc = new WineComServices();
		WineTypesServiceParcel parcel = svc.getWineTypes();
		
		Message msg = new Message();
		msg.obj = parcel;
		
		try{
			msgr.send( msg );
		}catch( RemoteException $e ){
			// handle no results
			$e.printStackTrace();
		}

	}

}
