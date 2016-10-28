package com.yno.wizard.model.service;

import android.os.Parcel;
import android.os.Parcelable;

public class VintagesServiceParcel extends AsyncServiceParcel implements
		Parcelable {
	
	public final static String TAG = "VintagesServiceParcel";
	public final static String NAME = VintagesServiceParcel.class.getName();
	
	public static final Parcelable.Creator<VintagesServiceParcel> CREATOR =
	    	new Parcelable.Creator<VintagesServiceParcel>() {
	            public VintagesServiceParcel createFromParcel(Parcel in) {
	                return new VintagesServiceParcel(in);
	            }
	 
	            public VintagesServiceParcel[] newArray(int size) {
	            	 return new VintagesServiceParcel[size];
	            }
	        };
	
    public VintagesServiceParcel(){
		super();
	}
	
	public VintagesServiceParcel( Parcel $in ){
		this();
		readFromParcel( $in );
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);

	}
	
	@Override
	protected void readFromParcel(Parcel $in) {
		super.readFromParcel($in);

	};

}
