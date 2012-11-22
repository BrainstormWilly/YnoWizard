package com.yno.wizard.model.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.WineParcel;

public class WineBarcodeSearchService extends IntentService {
	
	public static final String TAG = WineBarcodeSearchService.class.getSimpleName();

	public WineBarcodeSearchService(){
		super( TAG );
	}

	@Override
	protected void onHandleIntent(Intent $intent) {
		
		Bundle extras = $intent.getExtras();
		Messenger msgr = (Messenger) extras.get("android.os.Messenger");
		SearchWineParcel parcel = $intent.getParcelableExtra(SearchWineParcel.NAME);
		GoogleServices svc = new GoogleServices();
		WineDetailsServiceParcel wdPrcl = svc.getWineByBarcode(parcel.query);
		
		Message msg = new Message();
		msg.obj = wdPrcl.wine;
		
		try{
			msgr.send( msg );
		}catch( RemoteException $e ){
			// handle no results
			$e.printStackTrace();
		}

	}

}
