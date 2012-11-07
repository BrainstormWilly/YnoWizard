package com.yno.wizard.view;

import java.util.UUID;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.yno.wizard.controller.StartFacebookRatingCommand;
import com.yno.wizard.model.VintagesModel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.ActionBarHelper;
import com.yno.wizard.utils.AsyncUploadImage;
import com.yno.wizard.utils.ManualWineValidator;
import com.yno.wizard.utils.SpinnerAdapter;


public class ManualEntryActivity extends SherlockActivity implements IActionBarActivity {

	public static final String TAG = ManualEntryActivity.class.getSimpleName();
	public static final String NAME = "com.yno.wizard.intent.OPEN_MANUAL_ENTRY";
	public static Bitmap BMP;
	
	private OnEditorActionListener editorLstnr = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if( actionId==EditorInfo.IME_ACTION_DONE ){
				ManualEntryActivity.this.addWine();
				return true;
			}
			return false;
		}
	};
	private EditText _nameET;
	private EditText _typeET;
	private EditText _prodET;
	private EditText _descrET;
	private Spinner _yearSpn;
	//private Button _addImageBtn;
	private Button _submitBtn;
	private ImageButton _imageIB;
	private WineParcel _wine;
	private ActionBarHelper _abHelper;
	private AlertDialog _alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_entry_main);
		
		_abHelper = new ActionBarHelper(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		_wine = new WineParcel();
		_wine.manual = true;
		
		_imageIB = (ImageButton) findViewById(R.id.manualImageIB);
		
		_nameET = (EditText) findViewById(R.id.manualNameACT);	
		_typeET = (EditText) findViewById(R.id.manualTypeACT);	
		_prodET = (EditText) findViewById(R.id.manualProducerACT);
		_descrET = (EditText) findViewById(R.id.manualDescrACT);
		
		_nameET.setOnEditorActionListener( editorLstnr );
		_typeET.setOnEditorActionListener( editorLstnr );
		_prodET.setOnEditorActionListener( editorLstnr );
		
		_yearSpn = (Spinner) findViewById(R.id.manualVintageSpn);
		VintagesModel model = new VintagesModel();
		SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1, model.getAllVintages());
		_yearSpn.setAdapter(adapter);
		
		_imageIB.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, ActionBarHelper.CAMERA_REQUEST);
				}
			}	
		);
		
		_submitBtn = (Button) findViewById(R.id.manualReviewBtn);
		_submitBtn.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ManualEntryActivity.this.addWine();
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
	
	public void dismissProgress( boolean $foundWine ){
		// not used
	}
	
	public void showProgress( String $msg ){
		// not used
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_manual_menu, menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return _abHelper.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int $reqCode, int $resCode, Intent $intent) {
		_abHelper.checkActivityResult($reqCode, $resCode, $intent);
		if($reqCode==ActionBarHelper.CAMERA_REQUEST){
			if( $resCode==RESULT_OK){
				BMP = (Bitmap) $intent.getExtras().get("data");
				_imageIB.setImageBitmap(BMP);
				
				String filename = "yw_" + UUID.randomUUID().toString() + ".jpg";
				_wine.imageSmall = 
						_wine.imageLarge = getString( R.string.url_uploads ) + filename;
				
				new AsyncUploadImage(this, filename).execute(BMP);
			}
		}
	}
	
	private void addWine(){
		ManualWineValidator vldtr = new ManualWineValidator();
		vldtr.addField(_prodET, getString(R.string.requires_a_producer));
		vldtr.addField(_typeET, getString(R.string.requires_a_wine_type));

		
		if(vldtr.isValidated()){
			_wine.varietal = _typeET.getText().toString();
			_wine.producer = _prodET.getText().toString();
			_wine.description = _descrET.getText().toString();
			String year = _yearSpn.getSelectedItem().toString();
			try{
				_wine.year = Integer.parseInt(year);
			}catch( NumberFormatException $e ){
				_wine.year = 0;
				year = getString(R.string.nv);
			}
			_wine.nameQualified =
			_wine.name = year.trim() + " " + _wine.producer.trim() + " " + _nameET.getText().toString().trim() + " " + _typeET.getText().toString().trim();
			StartFacebookRatingCommand cmd = new StartFacebookRatingCommand(this);
			cmd.payload.putParcelable(WineParcel.NAME, _wine);
			cmd.execute();
		}else{
			
			AlertDialog.Builder bldr = new AlertDialog.Builder(this);
			
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
			
			TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
			TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
			Button btn = (Button) layout.findViewById(R.id.dialogAlertBtn);
			
			title.setText( getString(R.string.insufficient_information) );
			subtitle.setText(vldtr.toErrString());
			
			btn.setOnClickListener(
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
	}
}
