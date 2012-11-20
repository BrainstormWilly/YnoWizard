package com.yno.wizard.model.service;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class PhraseServiceParcel extends AsyncServiceParcel implements
		Parcelable {

	public final static String TAG = "PhraseServiceParcel";
	public final static String NAME = PhraseServiceParcel.class.getName();
	
	public static final Parcelable.Creator<PhraseServiceParcel> CREATOR =
	    	new Parcelable.Creator<PhraseServiceParcel>() {
	            public PhraseServiceParcel createFromParcel(Parcel in) {
	                return new PhraseServiceParcel(in);
	            }
	 
	            public PhraseServiceParcel[] newArray(int size) {
	            	 return new PhraseServiceParcel[size];
	            }
	        };
	        
	public ArrayList<String> values = new ArrayList<String>();
	
	
	public PhraseServiceParcel(){
		super();
	}
	
	public PhraseServiceParcel( Parcel $in ){
		this();
		readFromParcel( $in );
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		arg0.writeList(values);
	}
	
	@Override
	protected void readFromParcel(Parcel $in) {
		super.readFromParcel($in);
		$in.readList(values, String.class.getClassLoader());
		
	};
}
