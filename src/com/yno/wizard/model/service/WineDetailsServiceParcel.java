package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.WineParcel;

import android.os.Parcel;
import android.os.Parcelable;

public class WineDetailsServiceParcel extends AsyncServiceParcel implements
		Parcelable {
	
	public final static String TAG = "WineDetailsServiceParcel";
	public final static String NAME = WineDetailsServiceParcel.class.getName();
	
	public static final Parcelable.Creator<WineDetailsServiceParcel> CREATOR =
	    	new Parcelable.Creator<WineDetailsServiceParcel>() {
	            public WineDetailsServiceParcel createFromParcel(Parcel in) {
	                return new WineDetailsServiceParcel(in);
	            }
	 
	            public WineDetailsServiceParcel[] newArray(int size) {
	            	 return new WineDetailsServiceParcel[size];
	            }
	        };
	        
	public int isFromBarcode = 1;
	public WineParcel wine = new WineParcel();
	
	
	public WineDetailsServiceParcel(){
		super();
	}
	
	public WineDetailsServiceParcel( Parcel $in ){
		this();
		readFromParcel( $in );
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		arg0.writeInt(isFromBarcode);
		arg0.writeParcelable(wine, arg1);
	}
	
	@Override
	protected void readFromParcel(Parcel $in) {
		super.readFromParcel($in);
		isFromBarcode = $in.readInt();
		wine = $in.readParcelable(WineParcel.class.getClassLoader());	
	};

}
