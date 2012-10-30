package com.yno.wizard.model.fb;

import com.yno.wizard.model.ProducerParcel;

import android.os.Parcel;
import android.os.Parcelable;

public class FbUserParcel implements Parcelable {
	
	public final static String TAG = FbUserParcel.class.getSimpleName();
	public final static String NAME = FbUserParcel.class.getName();
	
	public static final Parcelable.Creator<FbUserParcel> CREATOR =
	    	new Parcelable.Creator<FbUserParcel>() {
	            public FbUserParcel createFromParcel(Parcel in) {
	                return new FbUserParcel(in);
	            }
	 
	            public FbUserParcel[] newArray(int size) {
	                return new FbUserParcel[size];
	            }
	        };
	        
	public String birthday = "";
	public String id = "";
	public String name = "";
	public String first_name = "";
	public String last_name = "";
	public String location = "";
	public String link =  "";
	public String picture =  "";
	public String username = "";
	public String gender = "";
	public String locale = "";

    public FbUserParcel(){;}
	
	public FbUserParcel( Parcel $in ){
		readFromParcel($in);
	}
	
	public String toUserString(){
		String user = first_name + " " + last_name + "\n"
				+ "Home City: " + location + "\n"
				+ "Gender: " + gender + "\n"
				+ "Birthday: " + birthday;
		return user;
	}
	    	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeString(birthday);
		$dest.writeString(id);
		$dest.writeString(name);
		$dest.writeString(first_name);
		$dest.writeString(last_name);
		$dest.writeString(location);
		$dest.writeString(link);
		$dest.writeString(picture);
		$dest.writeString(username);
		$dest.writeString(gender);
		$dest.writeString(locale);
	}
	
	private void readFromParcel(Parcel $in) {
		birthday = $in.readString();
		id = $in.readString();
		name = $in.readString();
		first_name = $in.readString();
		last_name = $in.readString();
		location = $in.readString();
		link = $in.readString();
		picture = $in.readString();
		username = $in.readString();
		gender = $in.readString();
		locale = $in.readString();
	}

}
