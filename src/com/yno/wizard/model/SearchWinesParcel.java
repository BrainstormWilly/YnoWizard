package com.yno.wizard.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yno.wizard.model.service.SearchData;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SearchWinesParcel implements Parcelable {

	public final static String TAG = SearchWinesParcel.class.getSimpleName();
	public final static String NAME = SearchWinesParcel.class.getName();
	
	public static final Parcelable.Creator<SearchWinesParcel> CREATOR =
	    	new Parcelable.Creator<SearchWinesParcel>() {
	            public SearchWinesParcel createFromParcel(Parcel in) {
	                return new SearchWinesParcel(in);
	            }
	 
	            public SearchWinesParcel[] newArray(int size) {
	            	 return new SearchWinesParcel[size];
	            }
	        };
	        
	public int page = 1;
	public String name = "";
	public String api = "";
	public List<WineParcel> results = new ArrayList<WineParcel>();
	public SearchTypeParcel type;
	public int value = 0;
	public String country = "";
	public String state = "";
	public String zip = ""; 
	public String ip = ""; 
	
	public boolean hasQuery(){
		return !name.equals("") || type!=null;
	}

	
	public SearchWinesParcel(){

	}
	
	public SearchWinesParcel( Parcel $in ){
		this();
		readFromParcel($in);
	}


	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeString(name);
		$dest.writeInt(page);
		$dest.writeTypedList(results);
		$dest.writeString(api);
		$dest.writeInt(value);
		$dest.writeString(country);
		$dest.writeString(state);
		$dest.writeString(zip);
		$dest.writeParcelable(type, $flags);
		$dest.writeString(ip);
	}
	
	public String getQuery(){
		String str = ProducerFactory.getProducerBase( name );
		return typedQuery(str);
		
	}
	
	public String getFullQuery(){
		String str = name;
		return typedQuery(str);
		
	}
	
	private String typedQuery( String $name ){
		if( type!=null && type.id>0 ){
			Iterator<String> itr = type.aliases.iterator();
			while( itr.hasNext() ){
				$name += " " + itr.next();
			}
		}
			
		return $name.trim();
	}
	
	private void readFromParcel(Parcel $in) {
		name = $in.readString();
		page = $in.readInt();
		$in.readTypedList(results, WineParcel.CREATOR);
		api = $in.readString();
		value = $in.readInt();
		country = $in.readString();
		state = $in.readString();
		zip = $in.readString();
		type = $in.readParcelable(SearchTypeParcel.class.getClassLoader());
		ip = $in.readString();
	}

}
