package com.yno.wizard.view;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.google.zxing.client.android.Intents.Scan;
import com.yno.wizard.R;
import com.yno.wizard.controller.DoBarcodeSearchCommand;
import com.yno.wizard.controller.DoWineSelectCommand;
import com.yno.wizard.controller.OpenManualEntryCommand;
import com.yno.wizard.controller.OpenPhraseSearchCommand;
import com.yno.wizard.controller.TrackSelectCommand;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.TrackingService;

public class ActionBarHelper {

	public static String TAG = ActionBarHelper.class.getSimpleName();
	public static final int BARCODE_RESULT = 100;
	public static final int MANUAL_RESULT = 200;
	public static final int SEARCH_RESULT = 300;
	public static final int CAMERA_REQUEST = 400;
	
	
	private static class WineSelectHandler extends Handler{
		
		private WeakReference<IActionBarActivity> _activity;
		
		WineSelectHandler( IActionBarActivity $activity ){
			_activity = new WeakReference<IActionBarActivity>( $activity );
		}
		
		@Override
		public void handleMessage(Message $msg) {
			IActionBarActivity thisActivity = _activity.get();
			WineParcel parcel = (WineParcel) $msg.obj;
			thisActivity.dismissProgress();
			
			if( !parcel.name.equals("") ){
				TrackSelectCommand tmd = new TrackSelectCommand();
				tmd.payload.putParcelable(WineParcel.NAME, parcel);
				tmd.payload.putString("phrase", TrackingService.BARCODE);
				tmd.execute();
				
				DoWineSelectCommand cmd = new DoWineSelectCommand(thisActivity);
				cmd.payload.putParcelable( WineParcel.NAME, parcel);
				cmd.execute();
			}else{
				thisActivity.showAlert(R.string.no_wine_found, R.string.try_searching_by_name_or_manually_entering_wine_info);
			}
		}
	}
	
	private Intent _zxingInt = new Intent("com.google.zxing.client.android.SCAN");
	private IActionBarActivity _context;
	
	public ActionBarHelper(IActionBarActivity $context){
		_context = $context;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.actionbarBarcode : 
				_zxingInt.putExtra(Scan.MODE, Scan.PRODUCT_MODE);
				_context.startActivityForResult(_zxingInt, BARCODE_RESULT);
				return true;
			case R.id.actionbarManual :
				OpenManualEntryCommand manualCmd = new OpenManualEntryCommand( (Context) _context );
				manualCmd.execute();
				return true;
			case R.id.actionbarSearch :
				OpenPhraseSearchCommand searchCmd = new OpenPhraseSearchCommand( (Context) _context );
				searchCmd.execute();
				return true;
		}
		
		return false;
	}
	
	public boolean checkActivityResult(int $reqCode, int $resCode, Intent $intent){
		//Log.d(TAG, String.valueOf($reqCode) + ", " + String.valueOf($resCode) + ", " + $intent);
		if( $reqCode==BARCODE_RESULT ){
			if( $resCode==Activity.RESULT_OK){
				_context.showProgress( _context.getString(R.string.looking_up_wine_barcode) );
				SearchWineParcel parcel = new SearchWineParcel();
				parcel.type = SearchData.SEARCH_TYPE_BARCODE;
				parcel.query = $intent.getStringExtra(Scan.RESULT);
				
				DoBarcodeSearchCommand cmd = new DoBarcodeSearchCommand( (Context) _context );
				cmd.messenger = new Messenger( new WineSelectHandler( _context ) );
				cmd.payload.putParcelable(SearchWineParcel.NAME, parcel);
				cmd.execute();
			}
			return true;
		}
		
		return false;
	}
	
	
}
