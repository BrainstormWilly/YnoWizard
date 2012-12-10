package com.yno.wizard.view.assist;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Messenger;

import com.actionbarsherlock.view.MenuItem;
import com.google.zxing.client.android.Intents.Scan;
import com.yno.wizard.R;
import com.yno.wizard.controller.DoBarcodeScanCommand;
import com.yno.wizard.controller.DoBarcodeSearchCommand;
import com.yno.wizard.controller.OpenManualEntryCommand;
import com.yno.wizard.controller.OpenPhraseSearchCommand;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.view.IAlertActivity;
import com.yno.wizard.view.handler.WineSelectHandler;

public class ActionBarAssist {

	public static String TAG = ActionBarAssist.class.getSimpleName();
	public static final int BARCODE_RESULT = 100;
	public static final int MANUAL_RESULT = 200;
	public static final int SEARCH_RESULT = 300;
	public static final int CAMERA_REQUEST = 400;
	
	
	//private Intent _zxingInt = new Intent("com.google.zxing.client.android.SCAN");
	private WeakReference<Activity> _context;
	
	public ActionBarAssist(Activity $context){
		_context = new WeakReference<Activity>($context);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Activity ctx = _context.get();
		if( ctx==null )
			return false;
		
		switch(item.getItemId()){
			case R.id.actionbarBarcode : 
//				Intent zxingInt = new Intent("com.google.zxing.client.android.SCAN");
//				zxingInt.putExtra(Scan.MODE, Scan.PRODUCT_MODE);
//				ctx.startActivityForResult(zxingInt, BARCODE_RESULT);
				new DoBarcodeScanCommand(ctx).execute();
				return true;
			case R.id.actionbarManual :
				new OpenManualEntryCommand( ctx ).execute();
				return true;
			case R.id.actionbarSearch :
				new OpenPhraseSearchCommand( ctx ).execute();
				return true;
		}
		
		return false;
	}
	
	public boolean checkActivityResult(int $reqCode, int $resCode, Intent $intent){
		//Log.d(TAG, String.valueOf($reqCode) + ", " + String.valueOf($resCode) + ", " + $intent);
		Activity ctx = _context.get();
		IAlertActivity alertCtx;
		if( ctx==null )
			return false;
		
		try{
			alertCtx = (IAlertActivity) ctx;
			if( $reqCode==BARCODE_RESULT ){
				if( $resCode==Activity.RESULT_OK){
					alertCtx.getAlertAssist().alertShowProgress(R.string.looking_up_wine_barcode);
					SearchWineParcel parcel = new SearchWineParcel();
					parcel.type = SearchData.SEARCH_TYPE_BARCODE;
					parcel.query = $intent.getStringExtra(Scan.RESULT);
					
					DoBarcodeSearchCommand cmd = new DoBarcodeSearchCommand( ctx );	
					cmd.messenger = new Messenger( new WineSelectHandler( alertCtx ) );
					cmd.payload.putParcelable(SearchWineParcel.NAME, parcel);
					cmd.execute();
				}
				return true;
			}
		}catch( Exception $e ){
			$e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	
}
