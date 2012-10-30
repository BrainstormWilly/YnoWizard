package com.yno.wizard.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.yno.wizard.controller.StartFacebookRatingCommand;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.ActionBarHelper;
import com.yno.wizard.utils.AsyncDownloadImage;
import com.yno.wizard.R;

public class WineSelectActivity extends SherlockFragmentActivity implements IActionBarActivity {

	public static String TAG = WineSelectActivity.class.getSimpleName();
	public static final String NAV_DESCRIPTION = "Description";
	public static final String NAV_DETAILS = "Details";
	public static final String NAV_NOTES = "Tasting Notes";
	public static final String NAV_RATINGS = "Ratings";
	public static final String NAV_PRICES = "Prices";
	
	private static class WineSelectAdaptor extends FragmentStatePagerAdapter{
		
		private WineParcel _wine;
		private ArrayList<String> _subnav;
		
		
		public WineSelectAdaptor( FragmentManager $fm, WineParcel $wine, ArrayList<String> $subnav ){
			super( $fm );
			_wine = $wine;
			_subnav = $subnav;
			
		}
		
		@Override
		public Fragment getItem( int $index ){
			if( _subnav.get($index).equals(NAV_DESCRIPTION) )
				return WineSelectDescripFragment.newInstance(_wine, _subnav);
			else if( _subnav.get($index).equals(NAV_PRICES) )
				return WineSelectPricesFragment.newInstance(_wine, _subnav);
			else if( _subnav.get($index).equals(NAV_RATINGS) )
				return WineSelectRatingsFragment.newInstance(_wine, _subnav);
			else if( _subnav.get($index).equals(NAV_NOTES) )
				return WineSelectNotesFragment.newInstance(_wine, _subnav);
			
			return WineSelectDetailsFragment.newInstance(_wine, _subnav);
		}
		
		@Override
		public int getCount(){
			return _subnav.size();
		}
	}

	
	private TextView _nameTV;
	//private TextView _detailsTV;
	private ImageView _labelIV;
	private ImageView _sponsorIV;
	private ViewPager _pagerVP;
	private Button _rateBtn;
	private ActionBarHelper _abHelper;
	private WineParcel _wine;
	private AlertDialog _diag;
	
	public void setWine( WineParcel $wine ){
		_wine = $wine;
		
		ArrayList<String> subnav = new ArrayList<String>();
		if( _wine.prices.size()>0 )
			subnav.add(NAV_PRICES);
		subnav.add(NAV_DETAILS);
		if( _wine.description!=null && !_wine.description.equals("") )
			subnav.add(NAV_DESCRIPTION);
		if( !_wine.notes.equals("") )
			subnav.add(NAV_NOTES);
		if( _wine.ratings.size()>0 )
			subnav.add(NAV_RATINGS);		
		
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
		
		if( _wine.manual )
			_labelIV.setImageBitmap(ManualEntryActivity.BMP); 
		else
			new AsyncDownloadImage( _labelIV ).execute( _wine.imageLarge );
		
		WineSelectAdaptor adaptor = new WineSelectAdaptor(getSupportFragmentManager(), _wine, subnav);
		_pagerVP.setAdapter(adaptor);
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView( R.layout.wine_select_main );
		
		_abHelper = new ActionBarHelper(this);
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
						WineSelectActivity.this.enlargeLabel();
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
		return _abHelper.onOptionsItemSelected(item);
	}
	
	public void enlargeLabel(){
		
		AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.wine_image_enlarger, (ViewGroup) findViewById(R.id.wineImageEnlargeRL));
		
		ImageView iv = (ImageView) layout.findViewById(R.id.wineImageEnlargeIV);
		Button btn = (Button) layout.findViewById(R.id.wineImageEnlargeBtn);
		
		btn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						WineSelectActivity.this.dismissDiag();
					}
				}
		);
		new AsyncDownloadImage( iv ).execute( _wine.imageLarge );
		
		bldr.setView( layout );
		_diag = bldr.create();
		_diag.show();
	}
	
	
	public void dismissDiag(){
		_diag.dismiss();
	}
	
	public void dismissProgress( boolean $hasResults ){
		// not used
	}
	
	public void showProgress( String $msg ){
		// not used
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_abHelper.checkActivityResult(requestCode, resultCode, data);
	}
	
	
}
