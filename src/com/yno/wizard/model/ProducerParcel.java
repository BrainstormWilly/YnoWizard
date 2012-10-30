package com.yno.wizard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProducerParcel implements Parcelable {
	
	public final static String TAG = ProducerParcel.class.getSimpleName();
	public final static String NAME = ProducerParcel.class.getName();
	
	public static final Parcelable.Creator<ProducerParcel> CREATOR =
	    	new Parcelable.Creator<ProducerParcel>() {
	            public ProducerParcel createFromParcel(Parcel in) {
	                return new ProducerParcel(in);
	            }
	 
	            public ProducerParcel[] newArray(int size) {
	                return new ProducerParcel[size];
	            }
	        };
	        
	public String name = "";
	public String website = "";
	public String email = "";
	public String api = "";
	public String id = "";
	
	
	public ProducerParcel(){;}
	
	public ProducerParcel( Parcel $in ){
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		
		$dest.writeString(id);
		$dest.writeString(api);
		$dest.writeString(name);
		$dest.writeString(website);
		$dest.writeString(email);
	}
	
	private void readFromParcel(Parcel $in) {
		id = $in.readString();
		api = $in.readString();
		name = $in.readString();
		website = $in.readString();
		email = $in.readString();
	}

}
