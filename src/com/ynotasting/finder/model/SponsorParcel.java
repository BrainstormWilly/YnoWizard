 package com.ynotasting.finder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SponsorParcel implements Parcelable {

	public final static String TAG = SponsorParcel.class.getSimpleName();
	public final static String NAME = SponsorParcel.class.getName();
	
	public static final Parcelable.Creator<SponsorParcel> CREATOR =
	    	new Parcelable.Creator<SponsorParcel>() {
	            public SponsorParcel createFromParcel(Parcel in) {
	                return new SponsorParcel(in);
	            }
	 
	            public SponsorParcel[] newArray(int size) {
	                return new SponsorParcel[size];
	            }

	        };
	        
	public String name = "";
	public String url = "";
	public int logo = 0;
	public String apiId = "";
	
    public SponsorParcel(  ){ ; }
	
	public SponsorParcel( Parcel $in ){
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(url);
		dest.writeInt(logo);
		dest.writeString(apiId);
	}
	
	private void readFromParcel(Parcel in) {
		name = in.readString();
		url = in.readString();
		logo = in.readInt();
		apiId = in.readString();
	}
}
