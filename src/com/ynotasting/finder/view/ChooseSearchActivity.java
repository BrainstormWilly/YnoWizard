package com.ynotasting.finder.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.ref.WeakReference;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.zxing.client.android.Intents.Scan;
import com.ynotasting.finder.R;
import com.ynotasting.finder.controller.DoBarcodeSearchCommand;
import com.ynotasting.finder.controller.DoWineSelectCommand;
import com.ynotasting.finder.controller.OpenManualEntryCommand;
import com.ynotasting.finder.controller.OpenPhraseSearchCommand;
import com.ynotasting.finder.controller.StartBarcodeSearchCommand;
import com.ynotasting.finder.model.SearchWineParcel;
import com.ynotasting.finder.model.WineParcel;
import com.ynotasting.finder.model.service.SearchData;
import com.ynotasting.finder.utils.ActionBarHelper;

public class ChooseSearchActivity extends SherlockActivity implements IActionBarActivity {
	
	public static final String TAG = ChooseSearchActivity.class.getSimpleName();
	public static final String NAME = "com.ynotasting.finder.intent.OPEN_CHOOSE_SEARCH_METHOD";
	
	private static final int _REQUEST_SCAN_BARCODE = 100;
	
	private static class WineSelectHandler extends Handler{
		
		private WeakReference<ChooseSearchActivity> _activity;
		
		WineSelectHandler( ChooseSearchActivity $activity ){
			_activity = new WeakReference<ChooseSearchActivity>( $activity );
		}
		
		@Override
		public void handleMessage(Message $msg) {
			ChooseSearchActivity thisActivity = _activity.get();
			SearchWineParcel parcel = (SearchWineParcel) $msg.obj;
			thisActivity.dismissProgress( !parcel.wine.name.equals("") );
			
			if( !parcel.wine.name.equals("") ){
				DoWineSelectCommand cmd = new DoWineSelectCommand(thisActivity);
				cmd.payload.putParcelable( SearchWineParcel.NAME, (SearchWineParcel) $msg.obj );
				cmd.execute();
			}
		}
	}
	
	private ImageButton _barcodeBtn;
	private ImageButton _searchBtn;
	private ImageButton _manualBtn;
	private Intent _zxingInt = new Intent("com.google.zxing.client.android.SCAN");
	private ProgressDialog _prog;
	private ActionBarHelper _abHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin_search_main);
		
		_abHelper = new ActionBarHelper(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		bkg.setDither(true);
		ActionBar abar = getSupportActionBar();
		abar.setBackgroundDrawable(bkg);
		
		_barcodeBtn = (ImageButton) findViewById(R.id.beginSearchBarcodeBtn);
        _searchBtn = (ImageButton) findViewById(R.id.beginSearchSearchBtn);
        _manualBtn = (ImageButton) findViewById(R.id.beginSearchManualBtn);
        
        _barcodeBtn.setOnClickListener( 
        		new OnClickListener() {
					@Override
					public void onClick(View v) {
						_zxingInt.putExtra(Scan.MODE, Scan.PRODUCT_MODE);
						startActivityForResult(_zxingInt, ActionBarHelper.BARCODE_RESULT);
					}
				}
        );
        
        _searchBtn.setOnClickListener( 
        		new OnClickListener() {
					@Override
					public void onClick(View v) {
						OpenPhraseSearchCommand cmd = new OpenPhraseSearchCommand( ChooseSearchActivity.this );
						cmd.execute();
					}
				}
        );
        
        _manualBtn.setOnClickListener(
        		new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						OpenManualEntryCommand cmd = new OpenManualEntryCommand( ChooseSearchActivity.this );
						cmd.execute();
						
					}
				}
        );
        
        
	}
	
	public void onActivityResult( int $reqCode, int $resCode, Intent $intent ){
		_abHelper.checkActivityResult($reqCode, $resCode, $intent);
	}
	
	public void showProgress( String $title, String $msg ){
		_prog = ProgressDialog.show(this, $title, $msg, true);
	}
	
	public void dismissProgress( boolean $foundWine ){
		_prog.dismiss();
		if( !$foundWine ){
			AlertDialog alert = new AlertDialog.Builder(ChooseSearchActivity.this).create();
			alert.setTitle("No Wine Found");
			alert.setMessage("Try rescanning or adding the wine manually.");
			alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", 
    				new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}
    		);
			alert.show();
		}
	}
	
}
