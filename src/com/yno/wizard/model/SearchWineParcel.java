package com.yno.wizard.model;

import com.yno.wizard.model.service.SearchData;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchWineParcel implements Parcelable {

	public final static String TAG = SearchWineParcel.class.getSimpleName();
	public final static String NAME = SearchWineParcel.class.getName();
	
	public static final Parcelable.Creator<SearchWineParcel> CREATOR =
	    	new Parcelable.Creator<SearchWineParcel>() {
	            public SearchWineParcel createFromParcel(Parcel in) {
	                return new SearchWineParcel(in);
	            }
	 
	            public SearchWineParcel[] newArray(int size) {
	            	 return new SearchWineParcel[size];
	            }
	        };
	        
	public int page = 1;
	public String type = SearchData.SEARCH_TYPE_PHRASE;
	public String query = "";
	
	public WineParcel wine = new WineParcel();
	
//	public String getType(){
//		return _type;
//	}
//	public void setType(String $val){
//		_type = $val;
//	}
//	
//	public int getPage(){
//		return _page;
//	}
//	public void setPage( int $val ){
//		_page = $val;
//	}
//	
//	public String getQuery(){
//		return _query;
//	}
//	public void setQuery( String $val ){
//		_query = $val;
//	}
	
	
	
	
	public SearchWineParcel(){;}
	
	public SearchWineParcel( Parcel $in ){
		this();
		readFromParcel($in);
	}
	        
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeParcelable(wine, $flags);
		$dest.writeInt(page);
		$dest.writeString(type);
		$dest.writeString(query);
	}
	
	private void readFromParcel(Parcel $in) {
		wine = $in.readParcelable( WineParcel.class.getClassLoader() );
		page = $in.readInt();
		type = $in.readString();
		query = $in.readString();
	}

}
