package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class WinesPhraseSearchService extends IntentService {
	
	public final static String TAG = "WinesPhraseSearchService";
	
	public WinesPhraseSearchService(){
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		
		Bundle extras = arg0.getExtras();
		Messenger msgr = (Messenger) extras.get("android.os.Messenger");
		SearchWinesParcel swPrcl = (SearchWinesParcel) extras.getParcelable(SearchWinesParcel.NAME);
		WinesServiceParcel wcPrcl = new WinesServiceParcel();
		WinesServiceParcel snPrcl = new WinesServiceParcel();
		WinesServiceParcel vtPrcl = new WinesServiceParcel();
		ArrayList<WineParcel> wines = new ArrayList<WineParcel>();
		
		try{
			wcPrcl = new WineComServices().getWinesByQuery(swPrcl);
			wines.addAll( wcPrcl.qualified );
		}catch( Exception $e ){
			$e.printStackTrace();
		}
		
		try{
			snPrcl = new SnoothServices().getWinesByQuery(swPrcl);
			wines.addAll( snPrcl.qualified );
		}catch( Exception $e ){
			$e.printStackTrace();
		}
		
		try{
			vtPrcl = new VinTankServices().getWinesByQuery(swPrcl);
			wines.addAll( vtPrcl.qualified );
		}catch( Exception $e ){
			$e.printStackTrace();
		}
		
		if( wines.size()==0 ){
			wines.addAll( wcPrcl.unqualified );
			wines.addAll( snPrcl.unqualified );
			wines.addAll( vtPrcl.unqualified );
		}
		
		swPrcl.results = WineFactory.sortWines(wines, swPrcl);
		
		Message msg = new Message();
		msg.obj = swPrcl;
		
		try{
			msgr.send( msg );
		}catch( RemoteException $e ){
			// handle no results
			$e.printStackTrace();
		}

	}

}
