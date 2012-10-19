package com.ynotasting.finder.view;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ynotasting.finder.R;
import com.ynotasting.finder.controller.StartFacebookRatingCommand;
import com.ynotasting.finder.model.VintagesModel;
import com.ynotasting.finder.model.WineParcel;
import com.ynotasting.finder.utils.ActionBarHelper;
import com.ynotasting.finder.utils.AsyncUploadImage;
import com.ynotasting.finder.utils.ManualWineValidator;
import com.ynotasting.finder.utils.SpinnerAdapter;
import com.ynotasting.finder.utils.TextSearchACAdapter;
import com.ynotasting.finder.utils.WineTypesACAdapter;


public class ManualEntryActivity extends SherlockActivity implements IActionBarActivity {

	public static final String TAG = ManualEntryActivity.class.getSimpleName();
	public static final String NAME = "com.ynotasting.finder.intent.OPEN_MANUAL_ENTRY";

	
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
		
		_imageIB = (ImageButton) findViewById(R.id.manualImageIB);
		
		_nameET = (EditText) findViewById(R.id.manualNameACT);	
		_typeET = (EditText) findViewById(R.id.manualTypeACT);	
		_prodET = (EditText) findViewById(R.id.manualProducerACT);
		_descrET = (EditText) findViewById(R.id.manualDescrACT);
		
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
					
					ManualWineValidator vldtr = new ManualWineValidator();
					vldtr.addField(_prodET, "Requires a Producer, Winery, or Vineyard");
					vldtr.addField(_typeET, "Requires a Wine Type");
					
					if(vldtr.isValidated()){
						_wine.varietal = _typeET.getText().toString();
						_wine.producer = _prodET.getText().toString();
						_wine.description = _descrET.getText().toString();
						String year = _yearSpn.getSelectedItem().toString();
						try{
							_wine.year = Integer.parseInt(year);
						}catch( NumberFormatException $e ){
							_wine.year = 0;
							year = "NV";
						}
						_wine.nameQualified =
						_wine.name = year.trim() + " " + _wine.producer.trim() + " " + _nameET.getText().toString().trim() + " " + _typeET.getText().toString().trim();
						StartFacebookRatingCommand cmd = new StartFacebookRatingCommand(ManualEntryActivity.this);
						cmd.payload.putParcelable(WineParcel.NAME, _wine);
						cmd.execute();
					}else{
						
            			AlertDialog.Builder bldr = new AlertDialog.Builder(ManualEntryActivity.this);
            			
            			LayoutInflater inflater = (LayoutInflater) ManualEntryActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            			View layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) findViewById(R.id.dialogAlertRL));
            			
            			TextView title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
            			TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
            			Button btn = (Button) layout.findViewById(R.id.dialogAlertBtn);
            			
            			title.setText("Missing Information");
            			subtitle.setText(vldtr.toErrString());
            			
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
			}
		);
		
		
	}
	
	public void dismissProgress( boolean $foundWine ){
		// not used
	}
	
	public void showProgress( String $title, String $msg ){
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
				Bitmap img = (Bitmap) $intent.getExtras().get("data");
				_imageIB.setImageBitmap(img);
				
				String filename = "yw_" + UUID.randomUUID().toString() + ".jpg";
				_wine.imageSmall = 
						_wine.imageLarge = getString( R.string.url_uploads ) + filename;
				
				new AsyncUploadImage(this, filename).execute(img);
			}
		}
	}
}
