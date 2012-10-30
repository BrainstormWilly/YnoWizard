package com.yno.wizard.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchVarietalsParcel implements ISearchParcel {
	
	public final static String TAG = SearchVarietalsParcel.class.getSimpleName();
	public final static String NAME = SearchVarietalsParcel.class.getName();
	
	public static final Parcelable.Creator<SearchVarietalsParcel> CREATOR =
	    	new Parcelable.Creator<SearchVarietalsParcel>() {
	            public SearchVarietalsParcel createFromParcel(Parcel in) {
	                return new SearchVarietalsParcel(in);
	            }
	 
	            public SearchVarietalsParcel[] newArray(int size) {
	            	//not allowed
	                return null;
	            }
	        };
	        
	private int _page = 1;
	
	public List<VarietalParcel> results = new ArrayList<VarietalParcel>();
	
	public String getType(){
		return NAME;
	}
	
	public int getPage(){
		return _page;
	}
	public void setPage( int $val ){
		_page = $val;
	}
	
	
	
	
	public SearchVarietalsParcel(){;}
	
	public SearchVarietalsParcel( Parcel $in ){
		this();
		readFromParcel($in);
	}


	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeInt(_page);
		$dest.writeTypedList(results);

	}
	
	private void readFromParcel(Parcel $in) {
		_page = $in.readInt();
		$in.readTypedList(results, VarietalParcel.CREATOR);
	}

}
