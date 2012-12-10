package com.yno.wizard.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.yno.wizard.R;
import com.yno.wizard.controller.StartFacebookRatingCommand;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.AsyncDownloadImage;
import com.yno.wizard.view.adapter.WineSelectPagerAdapter;
import com.yno.wizard.view.assist.ActionBarAssist;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class WineSelectActivity extends AbstractAnalyticsFragmentActivity implements IAlertActivity, IDebugActivity {

	public static String TAG = WineSelectActivity.class.getSimpleName();

	
	private TextView _nameTV;
	//private TextView _detailsTV;
	private ImageView _labelIV;
	private ImageView _sponsorIV;
	private ViewPager _pagerVP;
	private Button _rateBtn;
	private ActionBarAssist _abAssist;
	private ActivityAlertAssist _alertAssist;
	private WineParcel _wine;
//	private AlertDialog _diag;
//	private AlertDialog _prog;
	
	public void setWine( WineParcel $wine ){
		_wine = $wine;
		
		ArrayList<Integer> subnav = new ArrayList<Integer>();
		if( _wine.prices.size()>0 )
			subnav.add(R.string.select_pager_subtitle_prices);
		subnav.add(R.string.select_pager_subtitle_details);
		if( _wine.description!=null && !_wine.description.equals("") )
			subnav.add(R.string.select_pager_subnav_description);
		if( !_wine.notes.equals("") )
			subnav.add(R.string.select_pager_subtitle_tasting_notes);
		if( _wine.ratings.size()>0 )
			subnav.add(R.string.select_pager_subtitle_ratings);		
		
		_rateBtn.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						StartFacebookRatingCommand cmd = new StartFacebookRatingCommand( WineSelectActivity.this );
						cmd.payload.putParcelable(WineParcel.NAME, _wine);
						cmd.execute();
					}
				}
		);
		
		_nameTV.setText( _wine.nameQualified );
		//Log.d(TAG, "sponsor logo = " + _wine.sponsor.logo);
		_sponsorIV.setImageDrawable( getResources().getDrawable(_wine.sponsor.logo) );
		
		new AsyncDownloadImage( _labelIV ).execute( _wine.imageLarge );
		
		WineSelectPagerAdapter adaptor = new WineSelectPagerAdapter(getSupportFragmentManager(), _wine, subnav);
		_pagerVP.setAdapter(adaptor);
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView( R.layout.wine_select_main );
		
		_abAssist = new ActionBarAssist(this);
		_alertAssist = new ActivityAlertAssist(this);
		
		_nameTV = (TextView) findViewById( R.id.wineHeaderNameTV );
		//_detailsTV = (TextView) findViewById( R.id.wineSelectDetailsTV );
		_labelIV = (ImageView) findViewById( R.id.wineHeaderLabelIV );
		_sponsorIV = (ImageView) findViewById(R.id.wineHeaderSponsorIV);
		_pagerVP = (ViewPager) findViewById(R.id.wineSelectDetailsVP);
		_rateBtn = (Button) findViewById(R.id.wineSelectRateBtn);
		
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		_labelIV.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						getAlertAssist().alertEnlargeLabel(_wine);
					}
				}
		);
		
		Intent intent = getIntent();
		WineParcel parcel = intent.getParcelableExtra(WineParcel.NAME);
		setWine( parcel );
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return _abAssist.onOptionsItemSelected(item);
	}
	
	public ActivityAlertAssist getAlertAssist(){
		return _alertAssist;
	}
	
//	public void enlargeLabel(){
//		
//		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
//		
//		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.wine_image_enlarger, (ViewGroup) findViewById(R.id.wineImageEnlargeRL));
//		
//		ImageView iv = (ImageView) layout.findViewById(R.id.wineImageEnlargeIV);
//		Button btn = (Button) layout.findViewById(R.id.wineImageEnlargeBtn);
//		
//		btn.setOnClickListener(
//				new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						_diag.dismiss();
//					}
//				}
//		);
//		new AsyncDownloadImage( iv ).execute( _wine.imageLarge );
//		
//		bldr.setView( layout );
//		_diag = bldr.create();
//		_diag.show();
//	}
//	
//	public void showAlert( int $title, int $body ){
//		AlertDialog.Builder bldr = new AlertDialog.Builder( this );
//		
//		
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
//		
//		TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
//		TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
//		Button okBtn = (Button) layout.findViewById(R.id.dialogAlertBtn);
//		
//		title.setText($title);
//		subtitle.setText($body);
//		
//		okBtn.setOnClickListener(
//				new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						_diag.dismiss();
//					}
//				}
//		);
//		
//		bldr.setView(layout);
//		_diag = bldr.create();
//		_diag.show();
//	}
//	
//	public void dismissProgress(){
//		_prog.dismiss();
//	}
//	
//	public void showProgress( String $msg ){
//		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
//		
//		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
//		
//		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
//		
//		title.setText($msg);
//		
//		bldr.setView( layout );
//		_prog = bldr.create();
//		_prog.show();
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_abAssist.checkActivityResult(requestCode, resultCode, data);
	}
	
	
}
