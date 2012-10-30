package com.yno.wizard.view;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.zxing.client.android.Intents.Scan;
import com.yno.wizard.controller.DoBarcodeSearchCommand;
import com.yno.wizard.controller.DoWineSelectCommand;
import com.yno.wizard.controller.OpenManualEntryCommand;
import com.yno.wizard.controller.OpenPhraseSearchCommand;
import com.yno.wizard.controller.StartBarcodeSearchCommand;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.utils.ActionBarHelper;
import com.yno.wizard.R;

public class ChooseSearchActivity extends SherlockActivity implements IActionBarActivity {
	
	public static final String TAG = ChooseSearchActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_CHOOSE_SEARCH_METHOD";
	
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
	private AlertDialog _prog;
	private AlertDialog _alert;
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
	
	public void showProgress( String $msg ){
		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
		
		title.setText($msg);
		
		
		bldr.setView( layout );
		_prog = bldr.create();
		_prog.show();
	}
	
	public void dismissProgress( boolean $foundWine ){
		_prog.dismiss();
		if( !$foundWine ){
			AlertDialog.Builder bldr = new AlertDialog.Builder(this);
			
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
			
			TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
			TextView body = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
			Button btn = (Button) layout.findViewById(R.id.dialogAlertBtn);
			
			title.setText(R.string.no_wine_found);
			body.setText(R.string.try_rescanning);
			btn.setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							_alert.dismiss();
						}
					}
			);
			
			
			bldr.setView( layout );
			_alert = bldr.create();
			_alert.show();
			
		}
	}
	
}
