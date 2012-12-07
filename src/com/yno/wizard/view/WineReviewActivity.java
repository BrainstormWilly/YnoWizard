package com.yno.wizard.view;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.os.Parcelable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.google.analytics.tracking.android.EasyTracker;
import com.yno.wizard.R;
import com.yno.wizard.controller.TrackReviewCommand;
import com.yno.wizard.model.AlertParcel;
import com.yno.wizard.model.LocationModel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.model.service.fb.FacebookService;
import com.yno.wizard.utils.AsyncDownloadImage;
import com.yno.wizard.view.assist.ActionBarAssist;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class WineReviewActivity extends AbstractAnalyticsFragmentActivity implements IFacebookActivity, IAlertActivity {

	public static final String TAG = WineReviewActivity.class.getSimpleName();
	public static String NAV_COMMENTS = "Comments";
	public static String NAV_RATING = "Rating";
	
	private static final int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	
	private static class WineReviewAdaptor extends FragmentStatePagerAdapter{
		
		private FbWineReviewParcel _review;
		private ArrayList<String> _subnav = new ArrayList<String>();
		
		public WineReviewAdaptor( FragmentManager $fm, FbWineReviewParcel $review ){
			super( $fm );
			_review = $review;
			_subnav.add(NAV_COMMENTS);
			_subnav.add(NAV_RATING);
		}
		
		@Override
		public Fragment getItem( int $index ){
			if( $index==0 )
				return WineReviewCommentFragment.newInstance(_review, _subnav);
			return WineReviewRatingFragment.newInstance(_review, _subnav);
		}
		
		@Override
		public int getCount(){
			return _subnav.size();
		}
		
		
	}
	
	private Handler _alertHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			AlertParcel parcel = (AlertParcel) msg.obj;
			if( parcel.alert_id==ActivityAlertAssist.ALERT_ID_REVIEW_PERMISSION ){
				if( parcel.alert_action==ActivityAlertAssist.ALERT_ACTION_OK ){
					_sendToFeed = parcel.getReviewSendToFeed();
					
					TrackReviewCommand cmd = new TrackReviewCommand();
					cmd.payload.putParcelable(FbWineReviewParcel.NAME, _review);
					cmd.payload.putParcelable(FbUserParcel.NAME, _user);
					cmd.execute();
					
					_fbSvc.publishReviewToList(_review);
				}
			}else if( parcel.alert_id==ActivityAlertAssist.ALERT_ID_REVIEW_FACEBOOK ){
				if( parcel.alert_action==ActivityAlertAssist.ALERT_ACTION_OK ){
					beginFbLogin();
				}else{
					finish();
				}
			}
		}
	};
	
	
	private Handler _fbHdl = new Handler(); 
	private FacebookService _fbSvc;
	private ImageView _labelIV;
	private ImageView _sponsorIV;
	private TextView _nameTV;
	private ViewPager _pagerVP;
	private Button _postBtn;
	private FbWineReviewParcel _review;
	private FbUserParcel _user;
	private ActionBarAssist _abAssist;
	private ActivityAlertAssist _alertAssist;
	//private AlertDialog _diag;
	//private AlertDialog _prog;
	private LocationModel _location;
	private boolean _sendToFeed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_review_main);
		
		_abAssist = new ActionBarAssist(this);
		_alertAssist = new ActivityAlertAssist(this, new Messenger(_alertHandler));
		_location = new LocationModel(this);
		
		LayerDrawable bkg = (LayerDrawable) getResources().getDrawable(R.drawable.yw_actionbar_bkg);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(bkg);
		
		_nameTV = (TextView) findViewById(R.id.wineHeaderNameTV);
		_labelIV = (ImageView) findViewById(R.id.wineHeaderLabelIV);
		_sponsorIV = (ImageView) findViewById(R.id.wineHeaderSponsorIV);
		_postBtn = (Button) findViewById(R.id.wineReviewPostBtn);
		_pagerVP = (ViewPager) findViewById(R.id.wineReviewVP);
		
		WineParcel wine = getIntent().getParcelableExtra(WineParcel.NAME);
		_review = new FbWineReviewParcel();
		_review.wine = wine;
		WineReviewAdaptor adaptor = new WineReviewAdaptor(getSupportFragmentManager(), _review);
		_pagerVP.setAdapter(adaptor);
		
		_nameTV.setText(wine.name);	
		
		if( _review.wine.manual )
			_labelIV.setImageBitmap( ManualEntryActivity.BMP );
		else
			new AsyncDownloadImage( _labelIV ).execute( wine.imageLarge );
		
		if( wine.sponsor!=null )
			_sponsorIV.setImageDrawable( getResources().getDrawable(wine.sponsor.logo) );
		
		_postBtn.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					_alertAssist.alertShowAlert(0, 0, ActivityAlertAssist.ALERT_ID_REVIEW_PERMISSION, ActivityAlertAssist.ALERT_TYPE_REVIEW_PERMISSION);
					
//					AlertDialog.Builder bldr = new AlertDialog.Builder(WineReviewActivity.this);
//					
//					LayoutInflater inflater = (LayoutInflater) WineReviewActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//					View layout = inflater.inflate(R.layout.wine_review_post_dialog, (ViewGroup) findViewById(R.id.wineReviewPostRL));
//					
//					Button noBtn = (Button) layout.findViewById(R.id.wineReviewPostCancelBtn);
//					Button yesBtn = (Button) layout.findViewById(R.id.wineReviewPostOkBtn);
//					CheckBox cb = (CheckBox) layout.findViewById(R.id.wineReviewPostCB);
//					
//					_sendToFeed = false;
//					
//					cb.setOnCheckedChangeListener(
//							new OnCheckedChangeListener() {
//								
//								@Override
//								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//									_sendToFeed = isChecked;
//								}
//							}
//					);
//					
//					noBtn.setOnClickListener(
//							new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									_diag.dismiss();
//								}
//							}
//					);
//					
//					yesBtn.setOnClickListener(
//							new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									TrackReviewCommand cmd = new TrackReviewCommand();
//									cmd.payload.putParcelable(FbWineReviewParcel.NAME, _review);
//									cmd.payload.putParcelable(FbUserParcel.NAME, _user);
//									cmd.execute();
//									
//									_fbSvc.publishReviewToList(_review);
//									_diag.dismiss();
//									//_prog = ProgressDialog.show(WineReviewActivity.this, "Posting review", "", true);
//								}
//							}
//					);
//					
//					bldr.setView( layout );
//					_diag = bldr.create();
//					_diag.show();
				}
			}
				
		);
		
		_labelIV.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//WineReviewActivity.this.enlargeLabel();
						_alertAssist.alertEnlargeLabel(_review.wine);
					}
				}
		);
		
		_fbSvc = new FacebookService( this );
		
		//temp
		//_fbSvc.clearSession();
		//_fbSvc.unauthorize();
		
		//.d(TAG, "Facebook session is valid? " + _fbSvc.isSessionValid());
		if( _fbSvc.isSessionValid() ){
			
			_fbSvc.getUserData();
			//_loginBtn.setText("FACEBOOK LOGOUT");
		}else{
			_alertAssist.alertShowAlert(
					R.string.log_into_facebook, 
					R.string.wine_reviews_can_only_be_stored_to_facebook, 
					ActivityAlertAssist.ALERT_ID_REVIEW_FACEBOOK, 
					ActivityAlertAssist.ALERT_TYPE_OK_CANCEL);
//			AlertDialog.Builder bldr = new AlertDialog.Builder(WineReviewActivity.this);
//			
//			LayoutInflater inflater = (LayoutInflater) WineReviewActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//			View layout = inflater.inflate(R.layout.dialog_alert_cancel, (ViewGroup) findViewById(R.id.dialogAlertCancelRL));
//			
//			TextView title = (TextView) layout.findViewById(R.id.dialogAlertCancelTitleTV);
//			TextView subtitle = (TextView) layout.findViewById(R.id.dialogAlertCancelSubtitleTV);
//			Button noBtn = (Button) layout.findViewById(R.id.dialogAlertCancelCancelBtn);
//			Button yesBtn = (Button) layout.findViewById(R.id.dialogAlertCancelOkBtn);
//			
//			title.setText(R.string.log_into_facebook);
//			subtitle.setText(R.string.wine_reviews_can_only_be_stored_to_facebook);
//			
//			noBtn.setOnClickListener(
//					new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							WineReviewActivity.this.finish();
//						}
//					}
//			);
//			
//			yesBtn.setOnClickListener(
//					new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							WineReviewActivity.this.beginFbLogin();
//						}
//					}
//			);
//			
//			bldr.setView( layout );
//			_diag = bldr.create();
//			_diag.show();
		}
		
		
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
	
	@Override
	protected void onPause(){
		super.onPause();
		_location.disable();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		_location.enable();
		_fbSvc.extendAccessTokenIfNeeded();
	}
	
	@Override
	protected void onActivityResult(int $request, int $result, Intent $intent) {
		
		if( !_abAssist.checkActivityResult($request, $result, $intent) ){
			//Log.d(TAG, "onActivityResult = " + $intent);
			_fbSvc.authorizeCallback($request, $result, $intent);
		}
			
	}
	
	@Override
	public void onDialogComplete(Bundle $data, String $service) {
		if( $service.equals("authorize") ){
			//.d(TAG, "authorize complete");
			//_diag.hide();
			_alertAssist.alertDismiss();
			_fbSvc.getUserData();
		}else if( $service.equals("publishReviewToFeed") ){
			Toast.makeText(getApplicationContext(), R.string.review_posted_to_your_feed, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onDialogError(DialogError $e, String $service) {
		//Log.d(TAG, "dialog error");
		$e.printStackTrace();
	}
	
	@Override
	public void onRequestComplete(Parcelable $parcel, String $service) {
		if( $service.equals("getUserData") ){
			final FbUserParcel user = (FbUserParcel) $parcel;
			_fbHdl.post(new Runnable() {
                 @Override
                 public void run() { 
                	 Address  addr = _location.getPrimaryAddress();
                	 _user = user;
                	if( addr!=null )
                		_user.location = addr.getPostalCode();
                 }
             });
		}else if( $service.equals("unauthorize") ){
			_fbHdl.post(new Runnable() {
                @Override
                public void run() { 
                	Toast.makeText(getApplicationContext(), R.string.logged_out_of_facebook, Toast.LENGTH_SHORT).show();
                }
            });
		}
	} 
	
	
	@Override
	public void onRequestComplete( String $response, String $service ) {
		//Log.d(TAG, "onRequestComplete: " + $service);
		if( $service.equals("publishReviewToList") ){
			final String resp = $response;
			_fbHdl.post(new Runnable() {
                @Override
                public void run() { 
                	try{
                		
                		JSONObject respObj = new JSONObject(resp);
                		_review.id = respObj.getString("id");
                		//WineReviewActivity.this.dismissProgress(true);
                		Toast.makeText(getApplicationContext(), R.string.review_posted_to_your_yno_wizard_activities, Toast.LENGTH_SHORT).show();
                		if( _sendToFeed )
                			_fbSvc.publishReviewToFeed(_review);
                	}catch(JSONException $e){
                		String msg = getString(R.string.unable_to_post_review);
                		try{
                			JSONObject respObj = new JSONObject(resp);
                			JSONObject errObj = respObj.getJSONObject("error");
                			msg = errObj.getString("message");
                		}catch(Exception $ee){
                			$ee.printStackTrace();
                		}
//                		AlertDialog alert = new AlertDialog.Builder(WineReviewActivity.this).create();
//            			alert.setTitle(getString(R.string.facebook_error));
//            			alert.setMessage(msg);
//            			alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", 
//                				new DialogInterface.OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										dialog.dismiss();
//									}
//								}
//                		);
//            			alert.show();
            			_alertAssist.alertShowAlert( getString(R.string.facebook_error), msg );

                	}
                	
                }
            });
		}
	}
	
	
	@Override
	public void onFacebookError(FacebookError $e, String $service) {
		//Log.d(TAG, "onFacebookError");
		$e.printStackTrace();
	}
	
	@Override
	public void onServiceCancel(String $service) {
		if($service.equals("authorize")){
			finish();
		}else if($service.equals("publishReviewToFeed")){
			Toast.makeText(getApplicationContext(), R.string.feed_post_cancelled, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onServiceException(Exception $e, String $service) {
		//Log.d(TAG, "onServiceException");
		$e.printStackTrace();
	}
	
	/*
	 * IAlertActivity methods
	 */
	public ActivityAlertAssist getAlertAssist(){
		return _alertAssist;
	}
	
//	public void enlargeLabel(){
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
//		new AsyncDownloadImage( iv ).execute( _review.wine.imageLarge );
//		
//		bldr.setView( layout );
//		_diag = bldr.create();
//		
//		_diag.show();
//	}
	
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
	
	private void beginFbLogin(){
		_fbSvc.authorize(AUTHORIZE_ACTIVITY_RESULT_CODE);
	}
	
}
