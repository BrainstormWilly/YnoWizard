package com.yno.wizard.view.handler;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.yno.wizard.R;
import com.yno.wizard.controller.DoWineSelectCommand;
import com.yno.wizard.controller.TrackSelectCommand;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.service.TrackingService;
import com.yno.wizard.view.IAlertActivity;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class WineSelectHandler extends Handler {

	private WeakReference<IAlertActivity> _activity;
	
	public WineSelectHandler( IAlertActivity $activity ){
		_activity = new WeakReference<IAlertActivity>( $activity );
	}

	
	@Override
	public void handleMessage(Message $msg) {
		
		IAlertActivity thisActivity = _activity.get();
		if( thisActivity==null )
			return;
		
		WineParcel parcel = (WineParcel) $msg.obj;
		thisActivity.getAlertAssist().alertDismiss();
		
		if( !parcel.name.equals("") ){
			TrackSelectCommand tmd = new TrackSelectCommand();
			tmd.payload.putParcelable(WineParcel.NAME, parcel);
			tmd.payload.putString("phrase", TrackingService.BARCODE);
			tmd.execute();
			
			DoWineSelectCommand cmd = new DoWineSelectCommand( (Activity) thisActivity );
			cmd.payload.putParcelable( WineParcel.NAME, parcel);
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
