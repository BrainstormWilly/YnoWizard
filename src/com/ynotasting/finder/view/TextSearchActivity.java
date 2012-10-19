package com.ynotasting.finder.view;

import java.lang.ref.WeakReference;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ynotasting.finder.R;
import com.ynotasting.finder.controller.DoPhraseSearchCommand;
import com.ynotasting.finder.controller.OpenSearchResultsCommand;
import com.ynotasting.finder.model.LocationModel;
import com.ynotasting.finder.model.ProducerFactory;
import com.ynotasting.finder.model.SearchTypeParcel;
import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.model.db.SearchTypesTable;
import com.ynotasting.finder.model.db.YnoDbOpenHelper;
import com.ynotasting.finder.utils.ActionBarHelper;
import com.ynotasting.finder.utils.CursorSpinnerAdapter;
import com.ynotasting.finder.utils.TextSearchACAdapter;


public class TextSearchActivity extends SherlockActivity implements IActionBarActivity {
	
	private static String TAG = TextSearchActivity.class.getSimpleName();
	public static final String NAME = "com.ynotasting.finder.intent.OPEN_PHRASE_SEARCH";
	
	private static class PhraseSearchHandler extends Handler{
		
		private WeakReference<TextSearchActivity> _activity;
		
		PhraseSearchHandler( TextSearchActivity $activity ){
			_activity = new WeakReference<TextSearchActivity>( $activity );
		}
		
		@Override
		public void handleMessage(Message $msg) {
			TextSearchActivity thisActivity = _activity.get();
			SearchWinesParcel parcel = (SearchWinesParcel) $msg.obj;
			thisActivity.dismissProgress( parcel.results.size()>0 );
			
			if( parcel.results.size()>0 ){
				OpenSearchResultsCommand cmd = new OpenSearchResultsCommand(thisActivity);
				cmd.payload.putParcelable(SearchWinesParcel.NAME, parcel);
				cmd.execute();
			}
		}
	}

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
	private ActionBarHelper _abHelper;
	private AlertDialog _prog;
	private AlertDialog _alert;
	
//	private Handler _wineTypesSearchHandler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			ArrayList<SearchTypeParcel> types = (ArrayList<SearchTypeParcel>) msg.obj;
//			populateTypeSpinner( _dbHelper.insertAndReturnSearchTypes(types) );
//			_spinnerProg.dismiss();
//		}
//	};
	
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView( R.layout.text_search_main );
		
		_abHelper = new ActionBarHelper(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		_dbHelper = new YnoDbOpenHelper(this);
		_location = new LocationModel(this);
		
		_searchFld = (AutoCompleteTextView) findViewById( R.id.textSearchATV );
		_searchFld.setThreshold(3);
		_searchFld.setAdapter( new TextSearchACAdapter(this, android.R.layout.simple_dropdown_item_1line) );
			
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
						TextSearchActivity.this.populateTypeSpinner();
						
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
							TextSearchActivity.this.showProgress("Searching...", "Looking for wines matching '" + parcel.getFullQuery() + "'");
							LocationModel loc = new LocationModel( TextSearchActivity.this );
							parcel.ip = loc.getLocalIpAddress();
							Address primAddr = loc.getPrimaryAddress();
							if( primAddr!=null ){
								parcel.country = primAddr.getCountryCode();
								parcel.state = loc.getStateCode( primAddr.getAdminArea() );
								parcel.zip = primAddr.getPostalCode();
							}
							DoPhraseSearchCommand cmd = new DoPhraseSearchCommand( TextSearchActivity.this );
							cmd.messenger = new Messenger( new PhraseSearchHandler(TextSearchActivity.this) );
							cmd.payload.putParcelable( SearchWinesParcel.NAME, parcel );
							cmd.execute();
						}else{
							AlertDialog alert = new AlertDialog.Builder(TextSearchActivity.this).create();
	            			alert.setTitle("Insufficient Information");
	            			alert.setMessage("Requires a wine type and/or phrase");
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
		return _abHelper.onOptionsItemSelected(item);
	}
	
	public void dismissProgress( boolean $foundWines ){
		_prog.dismiss();
		if( !$foundWines ){
			
			AlertDialog.Builder bldr = new AlertDialog.Builder(TextSearchActivity.this);
			
			LayoutInflater inflater = (LayoutInflater) TextSearchActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
			
			TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
			TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
			Button btn = (Button) layout.findViewById(R.id.dialogAlertBtn);
			
			title.setText("No Wines Found");
			subtitle.setText("Try reducing your search criteria.");
			
			btn.setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							_alert.dismiss();
						}
					}
			);
			
			_alert = bldr.create();
			_alert.show();
		}
	}
	
	public void showProgress( String $title, String $msg ){
		
		AlertDialog.Builder bldr = new AlertDialog.Builder(TextSearchActivity.this);
		
		LayoutInflater inflater = (LayoutInflater) TextSearchActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
		
		title.setText($msg);
		
		
		bldr.setView( layout );
		_prog = bldr.create();
		_prog.show();
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_abHelper.checkActivityResult(requestCode, resultCode, data);
	}
	
	private void populateTypeSpinner(){
		Cursor cursor = _dbHelper.getAllFromSearchTypes();
		if(cursor.moveToFirst()){
			CursorSpinnerAdapter adapter = new CursorSpinnerAdapter(this, android.R.layout.simple_spinner_item, cursor, new String[]{SearchTypesTable.COLUMN_NAME}, new int[]{android.R.id.text1}, 0);
			_typeSpn.setAdapter(adapter);
		}
		_spinnerIsSet = true;
		
	}

	
	
	
}
