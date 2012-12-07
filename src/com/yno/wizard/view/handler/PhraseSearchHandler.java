package com.yno.wizard.view.handler;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.yno.wizard.R;
import com.yno.wizard.controller.OpenSearchResultsCommand;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.view.IAlertActivity;

public class PhraseSearchHandler extends Handler {

	private WeakReference<IAlertActivity> _activity;
	
	public PhraseSearchHandler( IAlertActivity $activity ){
		_activity = new WeakReference<IAlertActivity>( $activity );
	}
	
	@Override
	public void handleMessage(Message $msg) {
		IAlertActivity thisActivity = _activity.get();
		SearchWinesParcel parcel = (SearchWinesParcel) $msg.obj;
		thisActivity.getAlertAssist().alertDismiss();
		
		if( parcel.results.size()>0 ){
			OpenSearchResultsCommand cmd = new OpenSearchResultsCommand( (Activity) thisActivity );
			cmd.payload.putParcelable(SearchWinesParcel.NAME, parcel);
			cmd.execute();
		}else{
			//thisActivity.showAlert(R.string.no_wines_found, R.string.try_reducing_your_search_criteria);
			thisActivity.getAlertAssist().alertShowAlert(R.string.no_wines_found, R.string.try_reducing_your_search_criteria);
		}
	}
}
