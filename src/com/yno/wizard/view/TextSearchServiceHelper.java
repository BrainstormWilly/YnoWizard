package com.yno.wizard.view;

import java.util.ArrayList;

import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.yno.wizard.controller.OpenSearchResultsCommand;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineFactory;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.service.AsyncServiceParcel;
import com.yno.wizard.model.service.IServiceContext;
import com.yno.wizard.model.service.SnoothAsyncService;
import com.yno.wizard.model.service.VinTankAsyncService;
import com.yno.wizard.model.service.WineComAsyncService;

public class TextSearchServiceHelper implements IServiceContext {
	
	public final static String TAG = "TextSearchServiceHelper";

	private TextSearchActivity _ctx;
	private SearchWinesParcel _parcel;
	private SnoothAsyncService _snSvc;
	private WineComAsyncService _wcSvc;
	private VinTankAsyncService _vtSvc;
	private ArrayList<WineParcel> _wines;
	
	public TextSearchServiceHelper( TextSearchActivity $ctx ){
		_ctx = $ctx;
		_wcSvc = new WineComAsyncService( this );
		_snSvc = new SnoothAsyncService( this );
		_vtSvc = new VinTankAsyncService( this );
		
		_wines = new ArrayList<WineParcel>();
		
	}
	
	public void begin( SearchWinesParcel $parcel ){
		_parcel = $parcel;
		_wcSvc.getWinesByQuery($parcel);
	}
	
	public void resume( AsyncServiceParcel $parcel ){
		
		if( $parcel.app_id.equals( WineComAsyncService.API_ID ) ){
			_wines.addAll( _wcSvc.lastQualified );
			_snSvc.getWinesByQuery(_parcel);	
			return;
		}else if( $parcel.app_id.equals( SnoothAsyncService.API_ID ) ){
			_wines.addAll( _snSvc.lastQualified );
			_vtSvc.getWinesByQuery(_parcel);
			return;
		}else{
			_wines.addAll( _vtSvc.lastQualified );
		}
		
		if( _wines.size()==0 ){
			_wines.addAll( _wcSvc.lastUnqualified );
			_wines.addAll( _snSvc.lastUnqualified );
			_wines.addAll( _vtSvc.lastUnqualified );
		}
		
		_ctx.dismissProgress(_wines.size()>0);
		
		if( _wines.size()>0 ){
			_wines = WineFactory.sortWines( _wines, _parcel );
			_parcel.results = _wines;
			
			OpenSearchResultsCommand cmd = new OpenSearchResultsCommand(_ctx);
			cmd.payload.putParcelable( SearchWinesParcel.NAME, _parcel );
			cmd.execute();
		}

	}
	
	
}
