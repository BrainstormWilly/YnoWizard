package com.yno.wizard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VarietalParcel implements Parcelable {
	
	public final static String TAG = VarietalParcel.class.getSimpleName();
	public final static String NAME = VarietalParcel.class.getName();
	
	public static final Parcelable.Creator<VarietalParcel> CREATOR =
	    	new Parcelable.Creator<VarietalParcel>() {
	            public VarietalParcel createFromParcel(Parcel in) {
	                return new VarietalParcel(in);
	            }
	 
	            public VarietalParcel[] newArray(int size) {
	                return new VarietalParcel[size];
	            }
	        };
	
	public String var_name = "";
	public long var_id = 0;
	public String var_type = "";
	
	public VarietalParcel(){;}
	
	public VarietalParcel(Parcel $in ){
		this();
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeString(var_name);
		$dest.writeLong(var_id);
		$dest.writeString(var_type);
	}
	
	private void readFromParcel(Parcel $in) {
		var_name = $in.readString();
		var_id = $in.readLong();
		var_type = $in.readString();
	}

}
