package com.yno.wizard.view.assist;

import java.lang.ref.WeakReference;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.yno.wizard.R;
import com.yno.wizard.controller.TrackReviewCommand;
import com.yno.wizard.model.AlertParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.utils.AsyncDownloadImage;
import com.yno.wizard.view.IAlertActivity;
import com.yno.wizard.view.WineReviewActivity;

public class ActivityAlertAssist {

	public static final int ALERT_ACTION_OK = 1;
	public static final int ALERT_ACTION_CANCEL = 2;
	
	public static final int ALERT_TYPE_OK = 10;
	public static final int ALERT_TYPE_OK_CANCEL = 11;
	public static final int ALERT_TYPE_REVIEW_PERMISSION = 12;
	public static final int ALERT_TYPE_PROGRESS = 13;
	
	public static final int ALERT_ID_DEFAULT = 100; 
	public static final int ALERT_ID_NO_WINE_FOUND = 101;
	public static final int ALERT_ID_LABEL = 102;
	public static final int ALERT_ID_REVIEW_PERMISSION = 103;
	public static final int ALERT_ID_REVIEW_FACEBOOK = 104;
	
	private WeakReference<IAlertActivity> _actv;
	private AlertDialog _alrt;
	private Messenger _msgr;
	//private int _alrtId = 100;
	//private AlertParcel _parcel;
	
	private Activity getActivity(){
		return (Activity) _actv.get();
	}
	
	
	/*
	 * Constructor
	 */
	public ActivityAlertAssist( IAlertActivity $actv ){
		_actv = new WeakReference<IAlertActivity>($actv);
	}

	public ActivityAlertAssist( IAlertActivity $actv, Messenger $msgr ){
		_actv = new WeakReference<IAlertActivity>($actv);
		_msgr = $msgr;
	}
	
	
	/*
	 * Methods
	 */
	public void alertEnlargeLabel( WineParcel $wine ){
		
		alertDismiss();
		
		final AlertParcel parcel = new AlertParcel();
		parcel.alert_id = ALERT_ID_LABEL;
		
		AlertDialog.Builder bldr = new AlertDialog.Builder( getActivity() );
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.wine_image_enlarger, (ViewGroup) getActivity().findViewById(R.id.wineImageEnlargeRL));
		
		ImageView iv = (ImageView) layout.findViewById(R.id.wineImageEnlargeIV);
		Button btn = (Button) layout.findViewById(R.id.wineImageEnlargeBtn);
		
		btn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_alrt.dismiss();
						parcel.alert_action = ALERT_ACTION_CANCEL;
						ActivityAlertAssist.this.alertAction( parcel );
					}
				}
		);
		new AsyncDownloadImage( iv ).execute( $wine.imageLarge );
		
		bldr.setView( layout );
		_alrt = bldr.create();
		_alrt.show();
	}
	
	
	public void alertDismiss(){
		if( _alrt!=null ){
			// guard against orientation change
			try{
				_alrt.dismiss();
			}catch( Exception $e ){
				$e.printStackTrace();
			}
				
			_alrt = null;
		}
	}
	
	public void alertShowProgress( int $msg ){
		
		alertDismiss();
		
		AlertDialog.Builder bldr = new AlertDialog.Builder( getActivity() );
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) getActivity().findViewById(R.id.dialogProgressRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
		
		title.setText($msg);
		
		bldr.setView( layout );
		_alrt = bldr.create();
		_alrt.show();
	}
	
	public void alertShowAlert( String $title, String $body ){
		alertShowAlert( $title, $body, ALERT_ID_DEFAULT, ALERT_TYPE_OK );
	}
	
	public void alertShowAlert( int $title, int $body ){
		alertShowAlert( $title, $body, ALERT_ID_DEFAULT, ALERT_TYPE_OK );
	}
	
	public void alertShowAlert( int $title, int $body, int $id ){
		alertShowAlert( $title, $body, $id, ALERT_TYPE_OK );
	}
	
	public void alertShowAlert( int $title, int $body, int $id, int $type ){
		Activity actv = getActivity();
		alertShowAlert( actv.getString($title), actv.getString($body), $id, $type );
	}
	
	public void alertShowAlert( String $title, String $body, int $id, int $type ){
		
		alertDismiss();
		
		final AlertParcel parcel = new AlertParcel();
		parcel.alert_id = $id;
		
		Activity actv = getActivity();
		
		
		if( actv==null )
			return;
		
		AlertDialog.Builder bldr = new AlertDialog.Builder( actv );
		LayoutInflater inflater = (LayoutInflater) actv.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
		
		View layout;
		Button okBtn;
		Button cnclBtn;
		TextView title;
		TextView subtitle;
		
		if( $type==ALERT_TYPE_REVIEW_PERMISSION ){
			parcel.setReviewSendToFeed(false);
			layout = inflater.inflate(R.layout.wine_review_post_dialog, (ViewGroup) actv.findViewById(R.id.wineReviewPostRL));
			cnclBtn = (Button) layout.findViewById(R.id.wineReviewPostCancelBtn);
			okBtn = (Button) layout.findViewById(R.id.wineReviewPostOkBtn);
			CheckBox cb = (CheckBox) layout.findViewById(R.id.wineReviewPostCB);
			cb.setOnCheckedChangeListener(
					new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							parcel.setReviewSendToFeed(isChecked);
						}
					}
			);
			
			cnclBtn.setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							_alrt.dismiss();
							parcel.alert_action = ALERT_ACTION_CANCEL;
							ActivityAlertAssist.this.alertAction( parcel );
						}
					}
			);
			
			okBtn.setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							parcel.alert_action = ALERT_ACTION_OK;
						}
					}
			);
		}if( $type==ALERT_TYPE_OK_CANCEL ){
			layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) actv.findViewById(R.id.dialogAlertCancelRL));
			title = (TextView) layout.findViewById(R.id.dialogAlertCancelTitleTV);
			subtitle = (TextView) layout.findViewById(R.id.dialogAlertCancelSubtitleTV);
			okBtn = (Button) layout.findViewById(R.id.dialogAlertCancelCancelBtn);
			cnclBtn = (Button) layout.findViewById(R.id.dialogAlertCancelOkBtn);
			cnclBtn.setOnClickListener(
					new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							_alrt.dismiss();
							parcel.alert_action = ALERT_ACTION_CANCEL;
							ActivityAlertAssist.this.alertAction(parcel);
						}
					}
			);
		}else{
			layout = inflater.inflate(R.layout.dialog_alert, (ViewGroup) actv.findViewById(R.id.dialogAlertRL));
			title = (TextView) layout.findViewById(R.id.dialogAlertTitleTV);
			subtitle = (TextView) layout.findViewById(R.id.dialogAlertSubtitleTV);
			okBtn = (Button) layout.findViewById(R.id.dialogAlertBtn);
			
		}
		
		title.setText( $title );
		subtitle.setText( $body );
		
		okBtn.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						_alrt.dismiss();
						parcel.alert_action = ALERT_ACTION_OK;
						ActivityAlertAssist.this.alertAction(parcel);
					}
				}
		);
	
		
		
		bldr.setView(layout);
		_alrt = bldr.create();
		_alrt.show();
		
	}
	
	public void alertAction( AlertParcel $parcel ){
//		IAlertActivity actv = _actv.get();
//		if( actv!=null )
//			actv.alertAction( _alrtId, $action);
		if( _msgr!=null ){
			Message msg = new Message();
			msg.obj = $parcel;
			try{
				_msgr.send( msg );
			}catch( RemoteException $e ){
				// handle no results
				$e.printStackTrace();
			}
		}
			
	}
	
	

}
