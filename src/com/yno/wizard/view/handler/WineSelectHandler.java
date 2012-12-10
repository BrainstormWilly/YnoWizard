package com.yno.wizard.view.handler;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yno.wizard.R;
import com.yno.wizard.controller.DoWineSelectCommand;
import com.yno.wizard.controller.TrackSelectCommand;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.TrackingService;
import com.yno.wizard.view.AbstractAnalyticsActivity;
import com.yno.wizard.view.IAlertActivity;
import com.yno.wizard.view.IDebugActivity;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class WineSelectHandler extends Handler {

	public static final String TAG = "WineSelectHandler";
	
	private WeakReference<IAlertActivity> _activity;
	
	
	
	public WineSelectHandler( IAlertActivity $activity ){
		_activity = new WeakReference<IAlertActivity>( $activity );
	}

	
	@Override
	public void handleMessage(Message $msg) {
		
		IAlertActivity thisActivity = _activity.get();
		if( thisActivity==null )
			return;
		
		//WineParcel parcel = (WineParcel) $msg.obj;
		SearchWineParcel parcel = (SearchWineParcel) $msg.obj;
		//thisActivity.getAlertAssist().alertDismiss();
		
		if( !parcel.wine.name.equals("") ){
			
			//Log.d(TAG, "Doing Wine Select Tracking: " + String.valueOf(!((AbstractAnalyticsActivity) thisActivity).isDebuggable()));
			
			if( !((IDebugActivity) thisActivity).isDebuggable() ){
				TrackSelectCommand tmd = new TrackSelectCommand();
				tmd.payload.putParcelable(SearchWineParcel.NAME, parcel);
				tmd.execute();
			}
			
			DoWineSelectCommand cmd = new DoWineSelectCommand( (Activity) thisActivity );
			cmd.payload.putParcelable( WineParcel.NAME, parcel.wine);
			cmd.execute();
		}else{
			thisActivity.getAlertAssist().alertShowAlert(
					R.string.no_wine_found, 
					R.string.try_searching_by_name_or_manually_entering_wine_info, 
					ActivityAlertAssist.ALERT_ID_NO_WINE_FOUND, 
					ActivityAlertAssist.ALERT_TYPE_OK);
		}
	}
}
