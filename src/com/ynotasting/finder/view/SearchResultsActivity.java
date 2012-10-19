package com.ynotasting.finder.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ynotasting.finder.R;
import com.ynotasting.finder.controller.DoPhraseSearchCommand;
import com.ynotasting.finder.controller.DoWineSelectCommand;
import com.ynotasting.finder.controller.ShowPriceVendorCommand;
import com.ynotasting.finder.controller.StartWineSelectCommand;
import com.ynotasting.finder.model.LocationModel;
import com.ynotasting.finder.model.PriceParcel;
import com.ynotasting.finder.model.SearchWineParcel;
import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.WineParcel;
import com.ynotasting.finder.utils.ActionBarHelper;
import com.ynotasting.finder.utils.AsyncDownloadImage;
import com.ynotasting.finder.utils.SearchResultsListAdaptor;

public class SearchResultsActivity extends SherlockActivity implements OnItemClickListener, IActionBarActivity {
	
	public final static String TAG = SearchResultsActivity.class.getSimpleName();
	public static final String NAME = "com.ynotasting.finder.intent.OPEN_SEARCH_RESULTS";
	
	private final static int _REQUEST_SEARCH_WINE = 10;
	
	private static class WineSelectHandler extends Handler{
		
		private WeakReference<SearchResultsActivity> _activity;
		
		WineSelectHandler( SearchResultsActivity $activity ){
			_activity = new WeakReference<SearchResultsActivity>( $activity );
		}
		
		@Override
		public void handleMessage(Message $msg) {
			SearchResultsActivity thisActivity = _activity.get();
			thisActivity.dismissProgress(true);
			DoWineSelectCommand cmd = new DoWineSelectCommand(thisActivity);
			cmd.payload.putParcelable( WineParcel.NAME, (WineParcel) $msg.obj );
			cmd.execute();
		}
	}

	private ListView resultsLV;
	private TextView titleTV;
	
	private List<WineParcel> _results = new ArrayList<WineParcel>();
	private AlertDialog _prog;
	private ActionBarHelper _abHelper;
	
	

	@Override
	public void onCreate( Bundle savedInstanceState ){
		
		super.onCreate(savedInstanceState);
		setContentView( R.layout.search_results_main );
		
		_abHelper = new ActionBarHelper(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		Bundle extras = getIntent().getExtras();
		SearchWinesParcel parcel = extras.getParcelable(SearchWinesParcel.NAME);
		
		resultsLV = (ListView) findViewById( R.id.searchResultsLV );
		titleTV = (TextView) findViewById( R.id.searchResultsTitle );
		
		String query = parcel.getFullQuery();
		if( parcel.value>0 )
			query += " under $" + (parcel.value+1);

		titleTV.setText( "Results for '" + query + "'" );
		
		_results = parcel.results;
		SearchResultsListAdaptor adapter = new SearchResultsListAdaptor(getApplicationContext(), _results);
		resultsLV.setAdapter(adapter);
		resultsLV.setOnItemClickListener( SearchResultsActivity.this );
		
	}
	
	public void dismissProgress( boolean $hasResults ){
		_prog.dismiss();
	}
	
	public void showProgress( String $title, String $msg ){
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
		return true;
		
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
		parcel.wine = _results.get(arg2);
		
		showProgress("Searching...", "Looking up wine '" + parcel.wine.nameQualified + "'");
		StartWineSelectCommand cmd = new StartWineSelectCommand( SearchResultsActivity.this );
		cmd.messenger = new Messenger( new WineSelectHandler(SearchResultsActivity.this) );
		cmd.payload.putParcelable(SearchWineParcel.NAME, parcel);
		cmd.execute();
	}

}
