package com.yno.wizard.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.zxing.client.android.Intents.Scan;
import com.yno.wizard.R;
import com.yno.wizard.controller.OpenManualEntryCommand;
import com.yno.wizard.controller.OpenPhraseSearchCommand;
import com.yno.wizard.utils.ActionBarHelper;

public class ChooseSearchActivity extends SherlockActivity implements IActionBarActivity {
	
	public static final String TAG = ChooseSearchActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_CHOOSE_SEARCH_METHOD";
	
	
	private ImageButton _barcodeBtn;
	private ImageButton _searchBtn;
	private ImageButton _manualBtn;
	private Intent _zxingInt = new Intent("com.google.zxing.client.android.SCAN");
	private AlertDialog _prog;
	private AlertDialog _alert;
	private ActionBarHelper _abHelper;
	private AdView _ad;
	//private Tracker _trkr;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin_search_main);
		
		//_ad = (AdView) this.findViewById(R.id.beginSearchAdView);
		//_ad.loadAd( new AdRequest() );
		
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
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
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
