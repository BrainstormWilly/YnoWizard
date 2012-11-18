package com.yno.wizard.model.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;

public class WinesPhraseSearchService extends IntentService {
	
	public static final String TAG = WinesPhraseSearchService.class.getSimpleName();
	
//	private Messenger _msgr;
//	private SearchWinesParcel _parcel;
//	private SnoothAsyncService _snSvc;
//	private WineComAsyncService _wcSvc;
//	private VinTankAsyncService _vtSvc;
//	private ArrayList<WineParcel> _wines = new ArrayList<WineParcel>();

	public WinesPhraseSearchService(){
		super( TAG );
	}

	
//	public void resume( String $id ){
//		if( $id.equals( WineComAsyncService.API_ID ) ){
//			_wines.addAll( _wcSvc.lastQualified );
//			_snSvc.getWinesByQuery(_parcel);	
//		}else if( $id.equals( SnoothAsyncService.API_ID ) ){
//			_wines.addAll( _snSvc.lastQualified );
//			_vtSvc.getWinesByQuery(_parcel);
//		}else{
//			_wines.addAll( _vtSvc.lastQualified );
//		}
//		
//		if( _wines.size()==0 ){
//			_wines.addAll( _wcSvc.lastUnqualified );
//			_wines.addAll( _snSvc.lastUnqualified );
//			_wines.addAll( _vtSvc.lastUnqualified );
//		}
//		
//		_wines = WineFactory.sortWines( _wines, _parcel );
//		
//		if( _parcel.page==1 )
//			_parcel.results = _wines;
//		else 
//			_parcel.results.addAll( _wines );
//		
//		Message msg = new Message();
//		msg.obj = _parcel;
//		
//		try{
//			_msgr.send( msg );
//		}catch( RemoteException $e ){
//			// handle no results
//			//$e.printStackTrace();
//		}
//		
//	}
	
	@Override
	public void onCreate() {
//		_wcSvc = new WineComAsyncService( this );
//		_snSvc = new SnoothAsyncService( this );
//		_vtSvc = new VinTankAsyncService( this );
	}

	
	@Override
	protected void onHandleIntent(Intent $intent) {
		
		Bundle extras = $intent.getExtras();
		//_msgr = (Messenger) extras.get("android.os.Messenger");
		//_parcel = $intent.getParcelableExtra(SearchWinesParcel.NAME);
		//_wines.clear();
		
		//_wcSvc.getWinesByQuery(_parcel);

	}
	

	
	

}
