package com.yno.wizard.view;

import android.app.AlertDialog;
import android.content.Context;
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
import com.yno.wizard.controller.DoBarcodeScanCommand;
import com.yno.wizard.controller.OpenManualEntryCommand;
import com.yno.wizard.controller.OpenPhraseSearchCommand;
import com.yno.wizard.view.assist.ActionBarAssist;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class ChooseSearchActivity extends AbstractAnalyticsActivity implements IAlertActivity, IDebugActivity {
	
	public static final String TAG = ChooseSearchActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_CHOOSE_SEARCH_METHOD";
	
	
	private ImageButton _barcodeBtn;
	private ImageButton _searchBtn;
	private ImageButton _manualBtn;
//	private Intent _zxingInt = new Intent("com.google.zxing.client.android.SCAN");
//	private AlertDialog _prog;
//	private AlertDialog _alert;
	private ActionBarAssist _abAssist;
//	private AdView _ad;
	private ActivityAlertAssist _alertAssist;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin_search_main);
		
		//_ad = (AdView) this.findViewById(R.id.beginSearchAdView);
		//_ad.loadAd( new AdRequest() );
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		bkg.setDither(true);
		ActionBar abar = getSupportActionBar();
		abar.setBackgroundDrawable(bkg);
		
		_abAssist = new ActionBarAssist(this);
		_alertAssist = new ActivityAlertAssist(this);
		
		_barcodeBtn = (ImageButton) findViewById(R.id.beginSearchBarcodeBtn);
        _searchBtn = (ImageButton) findViewById(R.id.beginSearchSearchBtn);
        _manualBtn = (ImageButton) findViewById(R.id.beginSearchManualBtn);
        
        _barcodeBtn.setOnClickListener( 
        		new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent zxingInt = new Intent("com.google.zxing.client.android.SCAN");
//						zxingInt.putExtra(Scan.MODE, Scan.PRODUCT_MODE);
//						startActivityForResult(zxingInt, ActionBarAssist.BARCODE_RESULT);
						new DoBarcodeScanCommand(ChooseSearchActivity.this).execute();
					}
				}
        );
        
        _searchBtn.setOnClickListener( 
        		new OnClickListener() {
					@Override
					public void onClick(View v) {
						new OpenPhraseSearchCommand( ChooseSearchActivity.this ).execute();
					}
				}
        );
        
        _manualBtn.setOnClickListener(
        		new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new OpenManualEntryCommand( ChooseSearchActivity.this ).execute();
					}
				}
        );
        
	}
	
	
	public void onActivityResult( int $reqCode, int $resCode, Intent $intent ){
		_abAssist.checkActivityResult($reqCode, $resCode, $intent);
	}
	
	public ActivityAlertAssist getAlertAssist(){
		return _alertAssist;
	}

	
}
