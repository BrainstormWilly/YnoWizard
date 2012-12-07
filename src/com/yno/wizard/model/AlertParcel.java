package com.yno.wizard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlertParcel implements Parcelable {

	public final static String TAG = AlertParcel.class.getSimpleName();
	public final static String NAME = AlertParcel.class.getName();
	
	public static final Parcelable.Creator<AlertParcel> CREATOR =
	    	new Parcelable.Creator<AlertParcel>() {
	            public AlertParcel createFromParcel(Parcel in) {
	                return new AlertParcel(in);
	            }
	 
	            public AlertParcel[] newArray(int size) {
	                return new AlertParcel[size];
	            }
	        };
	   
	
	
	public int alert_id = 100;
	public int alert_action = 1;
	public int alert_type = 10;
	
	private int _review_send_to_feed = 0;
	public boolean getReviewSendToFeed(){
		return _review_send_to_feed>0;
	}
	public void setReviewSendToFeed( boolean $val ){
		_review_send_to_feed = $val ? 1 : 0;
	}
	
	
    public AlertParcel(){;}
	
	public AlertParcel( Parcel $in ){
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		
		$dest.writeInt(alert_id);
		$dest.writeInt(alert_action);
		$dest.writeInt(alert_type);
		$dest.writeInt(_review_send_to_feed);
	}
	
	private void readFromParcel(Parcel $in) {
		alert_id = $in.readInt();
		alert_action = $in.readInt();
		alert_type = $in.readInt();
		_review_send_to_feed = $in.readInt();
	}

}
