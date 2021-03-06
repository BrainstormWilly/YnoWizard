package com.yno.wizard.view;

import java.lang.ref.WeakReference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.yno.wizard.R;
import com.yno.wizard.controller.DoWinesPhraseSearchCommand;
import com.yno.wizard.controller.OpenSearchResultsCommand;
import com.yno.wizard.model.LocationModel;
import com.yno.wizard.model.ProducerFactory;
import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.db.SearchTypesTable;
import com.yno.wizard.model.db.YnoDbOpenHelper;
import com.yno.wizard.utils.CursorSpinnerAdapter;
import com.yno.wizard.view.adapter.PhraseSearchArrayAdapter;
import com.yno.wizard.view.assist.ActionBarAssist;
import com.yno.wizard.view.assist.ActivityAlertAssist;
import com.yno.wizard.view.handler.PhraseSearchHandler;


public class PhraseSearchActivity extends AbstractAnalyticsActivity implements IAlertActivity, IDebugActivity {
	
	private static String TAG = PhraseSearchActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_PHRASE_SEARCH";
	
//	private static class PhraseSearchHandler extends Handler{
//		
//		private WeakReference<TextSearchActivity> _activity;
//		
//		PhraseSearchHandler( TextSearchActivity $activity ){
//			_activity = new WeakReference<TextSearchActivity>( $activity );
//		}
//		
//		@Override
//		public void handleMessage(Message $msg) {
//			TextSearchActivity thisActivity = _activity.get();
//			SearchWinesParcel parcel = (SearchWinesParcel) $msg.obj;
//			thisActivity.dismissProgress();
//			
//			if( parcel.results.size()>0 ){
//				OpenSearchResultsCommand cmd = new OpenSearchResultsCommand(thisActivity);
//				cmd.payload.putParcelable(SearchWinesParcel.NAME, parcel);
//				cmd.execute();
//			}else{
//				thisActivity.showAlert(R.string.no_wines_found, R.string.try_reducing_your_search_criteria);
//			}
//		}
//	}

	private AutoCompleteTextView _searchFld;
	private Button _findBtn;
	private Button _clearBtn;
	private CheckBox _typeCB;
	private CheckBox _underCB;
	private Spinner _typeSpn;
	private SeekBar _underSB;
	private TextView _underTV;
	private View _underTL;
	private YnoDbOpenHelper _dbHelper;
	//private ProgressDialog _spinnerProg;
	private boolean _spinnerIsSet = false;
	private LocationModel _location;
	private ActionBarAssist _abAssist;
	private ActivityAlertAssist _alertAssist;
//	private AlertDialog _prog;
//	private AlertDialog _alert;
	
	
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView( R.layout.text_search_main );
		
		_abAssist = new ActionBarAssist(this);
		_alertAssist = new ActivityAlertAssist(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		_dbHelper = new YnoDbOpenHelper(this);
		_location = new LocationModel(this);
		
		_searchFld = (AutoCompleteTextView) findViewById( R.id.textSearchATV );
		_searchFld.setThreshold(3);
		_searchFld.setAdapter( new PhraseSearchArrayAdapter(this, android.R.layout.simple_dropdown_item_1line) );
		_searchFld.setOnEditorActionListener(
				new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if( actionId==EditorInfo.IME_ACTION_SEARCH ){
							PhraseSearchActivity.this.initSearch();
							return true;
						}
						return false;
					}
				}
		);
			
		_clearBtn = (Button) findViewById(R.id.textSearchClearBtn);
		_clearBtn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_searchFld.setText("");
					}
				}
		);
		
		_typeSpn = (Spinner) findViewById(R.id.textSearchTypeSpn);
		
		
		_typeCB = (CheckBox) findViewById(R.id.textSearchTypeCB);
		_typeCB.setOnCheckedChangeListener(
			new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					_typeSpn.setVisibility( isChecked ? View.VISIBLE : View.GONE );
					if( isChecked && !_spinnerIsSet )
						PhraseSearchActivity.this.populateTypeSpinner();
						
				}
			}
		);
		
		_underTL = (View) findViewById(R.id.textSearchUnderTL);
		_underTV = (TextView) findViewById(R.id.textSearchUnderTV);
		
		_underSB = (SeekBar) findViewById(R.id.textSearchUnderSB);
		_underSB.setOnSeekBarChangeListener(
			new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					_underTV.setText("$" + (progress+10));
				}
			}
		);
		
		_underCB = (CheckBox) findViewById(R.id.textSearchUnderCB);
		_underCB.setOnCheckedChangeListener(
			new CompoundButton.OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					_underTL.setVisibility( isChecked ? View.VISIBLE : View.GONE );
				}
			}
		);
		
		_findBtn = (Button) findViewById( R.id.textSearchBtn );
		_findBtn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						PhraseSearchActivity.this.initSearch();
					}
				}
		);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_search_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Log.d(TAG, String.valueOf(item.getItemId()) );
		return _abAssist.onOptionsItemSelected(item);
	}
	
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
//						_alert.dismiss();
//					}
//				}
//		);
//		
//		bldr.setView(layout);
//		_alert = bldr.create();
//		_alert.show();
//	}
//	
//	public void dismissProgress(){
//		_prog.dismiss();
//	}
//	
//	public void showProgress( String $msg ){
//		
//		AlertDialog.Builder bldr = new AlertDialog.Builder(TextSearchActivity.this);
//		
//		LayoutInflater inflater = (LayoutInflater) TextSearchActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
//		
//		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
//		
//		title.setText($msg);
//		
//		
//		bldr.setView( layout );
//		_prog = bldr.create();
//		_prog.show();
//	}
	
	public ActivityAlertAssist getAlertAssist(){
		if( _alertAssist==null )
			_alertAssist = new ActivityAlertAssist(this);
		return _alertAssist;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.d(TAG, "onResume");
		getAlertAssist().alertDismiss();
		_location.enable();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		_location.disable();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_abAssist.checkActivityResult(requestCode, resultCode, data);
	}
	
	private void populateTypeSpinner(){
		Cursor cursor = _dbHelper.getAllFromSearchTypes();
		if(cursor.moveToFirst()){
			CursorSpinnerAdapter adapter = new CursorSpinnerAdapter(this, android.R.layout.simple_spinner_item, cursor, new String[]{SearchTypesTable.COLUMN_NAME}, new int[]{android.R.id.text1}, 0);
			_typeSpn.setAdapter(adapter);
		}
		_spinnerIsSet = true;
	}

	private void initSearch(){
		SearchWinesParcel parcel = new SearchWinesParcel();
		
		parcel.name = ProducerFactory.getProducerBase( _searchFld.getText().toString() );
		//Log.d(TAG, parcel.name);
		if( _typeCB.isChecked() ){
			Cursor cursor = (Cursor) _typeSpn.getSelectedItem();
			if( cursor==null ){
				_typeSpn.setSelection(0);
				cursor = (Cursor) _typeSpn.getSelectedItem();
			}
			SearchTypeParcel sParcel = new SearchTypeParcel();
			sParcel.id = cursor.getInt(cursor.getColumnIndex(SearchTypesTable.COLUMN_WINECOM_ID));
			sParcel.setName( cursor.getString(cursor.getColumnIndex(SearchTypesTable.COLUMN_NAME)) );
			parcel.type = sParcel;
		}
		if( _underCB.isChecked() )
			parcel.value = _underSB.getProgress()+10;
		if( parcel.hasQuery() ){
			//showProgress( getString(R.string.search_wines_matching) + parcel.getFullQuery() + "'" );
			getAlertAssist().alertShowProgress(getString(R.string.search_wines_matching) + parcel.getFullQuery() + "'");
			parcel.ip = _location.getLocalIpAddress();
			Address primAddr = _location.getPrimaryAddress();
			if( primAddr!=null ){
				parcel.country = primAddr.getCountryCode();
				parcel.state = _location.getStateCode( primAddr.getAdminArea() );
				parcel.zip = primAddr.getPostalCode();
			}
			
			DoWinesPhraseSearchCommand cmd = new DoWinesPhraseSearchCommand(this);
			cmd.messenger = new Messenger(
					new PhraseSearchHandler(PhraseSearchActivity.this)
			);
			cmd.payload.putParcelable(SearchWinesParcel.NAME, parcel);
			cmd.execute();
			
		}else{
//			AlertDialog.Builder bldr = new AlertDialog.Builder(TextSearchActivity.this);
//			
//			LayoutInflater inflater = (LayoutInflater) TextSearchActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//			View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
//			
//			TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
//			TextView body = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
//			Button btn = (Button) layout.findViewById(R.id.dialogAlertBtn);
//			
//			title.setText(R.string.insufficient_information);
//			body.setText(R.string.requires_search_criteria);
//			btn.setOnClickListener(
//					new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							_alert.dismiss();
//						}
//					}
//			);
//			
//			
//			bldr.setView( layout );
//			_alert = bldr.create();
//			_alert.show();
			getAlertAssist().alertShowAlert(R.string.insufficient_information, R.string.requires_search_criteria);
		}
	}
	
	
}
