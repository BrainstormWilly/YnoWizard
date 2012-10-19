package com.ynotasting.finder.model.service;

import java.util.ArrayList;

import com.ynotasting.finder.model.SearchWineParcel;
import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.WineParcel;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class WineSelectSearchService extends IntentService {

	public static final String TAG = WineSelectSearchService.class.getSimpleName();

	public WineSelectSearchService(){
		super( TAG );
	}
	
	@Override
	protected void onHandleIntent(Intent $intent) {
		
		Bundle extras = $intent.getExtras();
		Messenger msgr = (Messenger) extras.get("android.os.Messenger");
		SearchWineParcel parcel = $intent.getParcelableExtra(SearchWineParcel.NAME);
		WineParcel wine = parcel.wine;
		
		GoogleServiceClass svc = new GoogleServiceClass();
		WineParcel finalWine = svc.getWineDetails(wine);
		
		// do more apis
		
		Message msg = new Message();
		msg.obj = finalWine;
		try{
			msgr.send( msg );
		}catch( RemoteException $e ){
			// handle no results
			$e.printStackTrace();
		}
		
//		if( wine.api.equals( VinTankServiceClass.API_ID ) )
//			svc = new VinTankServiceClass();
//		else if( wine.api.equals( WineComServiceClass.API_ID ) )
//			svc = new WineComServiceClass();
//		else if( wine.api.equals( SnoothServiceClass.API_ID ) )
//			svc = new SnoothServiceClass();
		
		
	}

}
