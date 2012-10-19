package com.ynotasting.finder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegionParcel implements Parcelable {
	
	public final static String TAG = RegionParcel.class.getSimpleName();
	public final static String NAME = RegionParcel.class.getName();
	
	public static final Parcelable.Creator<RegionParcel> CREATOR =
	    	new Parcelable.Creator<RegionParcel>() {
	            public RegionParcel createFromParcel(Parcel in) {
	                return new RegionParcel(in);
	            }
	 
	            public RegionParcel[] newArray(int size) {
	                return new RegionParcel[size];
	            }
	        };
	
	public String id = "";
	public String name = "";
	public String parent = "";
	public String api = "";
	
	
	
	public RegionParcel(){;}
	
	public RegionParcel( Parcel $in ){
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeString(id);
		$dest.writeString(name);
		$dest.writeString(api);
		$dest.writeString(parent);
	}
	
	private void readFromParcel(Parcel $in) {
		id = $in.readString();
		name = $in.readString();
		api = $in.readString();
		parent = $in.readString();
	}

}
