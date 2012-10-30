package com.yno.wizard.model;

import com.yno.wizard.model.service.GoogleServiceClass;
import com.yno.wizard.model.service.SnoothServiceClass;
import com.yno.wizard.model.service.VinTankServiceClass;
import com.yno.wizard.model.service.WineComServiceClass;
import com.yno.wizard.R;

public class SponsorFactory {

	public static SponsorParcel createGoogleSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Google";
		parcel.url = "http://www.google.com/shopping";
		parcel.logo = R.drawable.sponsor_google;
		parcel.apiId = GoogleServiceClass.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createWineComSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Wine.com";
		parcel.url = "http://www.wine.com";
		parcel.logo = R.drawable.sponsor_winecom;
		parcel.apiId = WineComServiceClass.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createSnoothSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Snooth";
		parcel.url = "http://www.snooth.com";
		parcel.logo = R.drawable.sponsor_snooth;
		parcel.apiId = SnoothServiceClass.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createVintankSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "VinTank";
		parcel.url = "http://www.vintank.com";
		parcel.logo = R.drawable.sponsor_vintank;
		parcel.apiId = VinTankServiceClass.API_ID;
		
		return parcel;

	}
}
