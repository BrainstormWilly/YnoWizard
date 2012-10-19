package com.ynotasting.finder.model.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.WineFactory;
import com.ynotasting.finder.model.WineParcel;

public class WinesPhraseSearchService extends IntentService {
	
	public static final String TAG = WinesPhraseSearchService.class.getSimpleName();

	public WinesPhraseSearchService(){
		super( TAG );
	}
	
	@Override
	protected void onHandleIntent(Intent $intent) {
		
		Bundle extras = $intent.getExtras();
		Messenger msgr = (Messenger) extras.get("android.os.Messenger");
		SearchWinesParcel parcel = $intent.getParcelableExtra(SearchWinesParcel.NAME);
		ArrayList<WineParcel> wines = new ArrayList<WineParcel>();
		ArrayList<WineParcel> tempWines;
		
		
		IWineSearchService vtSvc = new VinTankServiceClass();
		IWineSearchService wcSvc = new WineComServiceClass();
		IWineSearchService snSvc = new SnoothServiceClass();
		
		// look for qualified results first
		// priorities = wine.com, snooth.com, vintank.com (if value not set)
		if( wines.size()<SearchData.API_CATEGORY_RESULTS ){
			tempWines = wcSvc.getWinesByQuery( parcel );
			wines.addAll( wcSvc.getLastQualifiedWines() );
			Log.i(TAG, "Added " + wcSvc.getLastQualifiedWines().size() + " Wine.com qualified wines." );
		}
		
		if( wines.size()<SearchData.API_CATEGORY_RESULTS ){
			tempWines = snSvc.getWinesByQuery( parcel );
			wines.addAll( snSvc.getLastQualifiedWines() );
			Log.i(TAG, "Added " + snSvc.getLastQualifiedWines().size() + " Snooth qualified wines." );
		}
		
		if( parcel.value==0 && wines.size()<SearchData.API_CATEGORY_RESULTS ){
			tempWines = vtSvc.getWinesByQuery( parcel );
			wines.addAll( vtSvc.getLastQualifiedWines() );
			Log.i(TAG, "Added " + vtSvc.getLastQualifiedWines().size() + " Vintank qualified wines." );
		}
		
		
		
		
		// if no qualified results look for any match
		// priorities = wine.com, snooth.com, vintank.com (if value not set)
		if( wines.size()==0 ){
			
			if( wines.size()<SearchData.API_CATEGORY_RESULTS ){
				wines.addAll( wcSvc.getLastUnqualifiedWines() );
				Log.i(TAG, "Added " + wcSvc.getLastUnqualifiedWines().size() + " Wine.com unqualified wines." );
			}
			
			if( wines.size()<SearchData.API_CATEGORY_RESULTS ){
				wines.addAll( snSvc.getLastUnqualifiedWines() );
				Log.i(TAG, "Added " + snSvc.getLastUnqualifiedWines().size() + " Snooth unqualified wines." );
			}
			
			if( parcel.value==0 && wines.size()<SearchData.API_CATEGORY_RESULTS ){
				wines.addAll( vtSvc.getLastUnqualifiedWines() );
				Log.i(TAG, "Added " + vtSvc.getLastUnqualifiedWines().size() + " Vintank unqualified wines." );
			}
		
		}
		
		// sort wines by relativity and name
		wines = WineFactory.sortWines( wines, parcel );
		
		if( parcel.page==1 )
			parcel.results = wines;
		else 
			parcel.results.addAll( wines );
		
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
