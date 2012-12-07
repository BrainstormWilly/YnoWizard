package com.yno.wizard.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.yno.wizard.R;
import com.yno.wizard.controller.DoWineSelectCommand;
import com.yno.wizard.controller.StartWineSelectCommand;
import com.yno.wizard.controller.TrackSelectCommand;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.adapter.SearchResultsListAdapter;
import com.yno.wizard.view.assist.ActionBarAssist;

public class SearchResultsActivity extends SherlockActivity implements OnItemClickListener, IActionBarActivity {
	
	public final static String TAG = SearchResultsActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_SEARCH_RESULTS";
	
	//private final static int _REQUEST_SEARCH_WINE = 10;
	
	private static class WineSelectHandler extends Handler{
		
		private WeakReference<SearchResultsActivity> _activity;
		
		WineSelectHandler( SearchResultsActivity $activity ){
			_activity = new WeakReference<SearchResultsActivity>( $activity );
		}
		
		@Override
		public void handleMessage(Message $msg) {
			SearchResultsActivity thisActivity = _activity.get();
			thisActivity.dismissProgress();
			DoWineSelectCommand cmd = new DoWineSelectCommand(thisActivity);
			cmd.payload.putParcelable( WineParcel.NAME, (WineParcel) $msg.obj );
			cmd.execute();
		}
	}

	private ListView resultsLV;
	//private TextView titleTV;
	
	//private List<WineParcel> _results = new ArrayList<WineParcel>();
	private AlertDialog _alert;
	private AlertDialog _prog;
	private ActionBarAssist _abHelper;
	private SearchWinesParcel _swPrcl = new SearchWinesParcel();
	private AdView _ad;

	@Override
	public void onCreate( Bundle savedInstanceState ){
		
		super.onCreate(savedInstanceState);
		setContentView( R.layout.search_results_main );
		
		//_ad = (AdView) this.findViewById(R.id.searchResultsAdView);
		//_ad.loadAd( new AdRequest() );
		
		_abHelper = new ActionBarAssist(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		Bundle extras = getIntent().getExtras();
		_swPrcl = extras.getParcelable(SearchWinesParcel.NAME);
		
		resultsLV = (ListView) findViewById( R.id.searchResultsLV );
//		titleTV = (TextView) findViewById( R.id.searchResultsTitle );
//		
//		_query = parcel.getFullQuery();
//		if( parcel.value>0 )
//			_query += " under $" + (parcel.value+1);
//
//		titleTV.setText( "Results for '" + _query + "'" );
		
		SearchResultsListAdapter adapter = new SearchResultsListAdapter(getApplicationContext(), _swPrcl.results);
		resultsLV.setAdapter(adapter);
		resultsLV.setOnItemClickListener( SearchResultsActivity.this );
		
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
	
	public void showAlert( int $title, int $body ){
		AlertDialog.Builder bldr = new AlertDialog.Builder( this );
		
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
		TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
		Button okBtn = (Button) layout.findViewById(R.id.dialogAlertBtn);
		
		title.setText($title);
		subtitle.setText($body);
		
		okBtn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_alert.dismiss();
					}
				}
		);
		
		bldr.setView(layout);
		_alert = bldr.create();
		_alert.show();
	}
	
	public void dismissProgress(){
		_prog.dismiss();
	}
	
	public void showProgress( String $msg ){
		//_prog = ProgressDialog.show(this, $title, $msg, true);
		AlertDialog.Builder bldr = new AlertDialog.Builder(SearchResultsActivity.this);
		
		LayoutInflater inflater = (LayoutInflater) SearchResultsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
		
		title.setText($msg);

		bldr.setView( layout );
		_prog = bldr.create();
		_prog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return _abHelper.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_abHelper.checkActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SearchWineParcel parcel = new SearchWineParcel();
		parcel.wine = _swPrcl.results.get(arg2);
		parcel.query = _swPrcl.getQuery();
		
		showProgress(getString(R.string.searching_for_wine) + parcel.wine.nameQualified + "'");
		
		TrackSelectCommand tmd = new TrackSelectCommand();
		tmd.payload.putParcelable(WineParcel.NAME, parcel.wine);
		tmd.payload.putString("phrase", parcel.query);
		tmd.execute();
		
		StartWineSelectCommand cmd = new StartWineSelectCommand( SearchResultsActivity.this );
		cmd.messenger = new Messenger( new WineSelectHandler(SearchResultsActivity.this) );
		cmd.payload.putParcelable(SearchWineParcel.NAME, parcel);
		cmd.execute();
	}

}
