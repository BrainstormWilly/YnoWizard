package com.yno.wizard.model;

import com.yno.wizard.R;
import com.yno.wizard.model.service.GoogleServices;
import com.yno.wizard.model.service.SnoothServices;
import com.yno.wizard.model.service.VinTankServices;
import com.yno.wizard.model.service.WineComServices;

public class SponsorFactory {

	public static SponsorParcel createGoogleSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Google";
		parcel.url = "http://www.google.com/shopping";
		parcel.logo = R.drawable.sponsor_google;
		parcel.apiId = GoogleServices.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createWineComSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Wine.com";
		parcel.url = "http://www.wine.com";
		parcel.logo = R.drawable.sponsor_winecom;
		parcel.apiId = WineComServices.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createSnoothSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "Snooth";
		parcel.url = "http://www.snooth.com";
		parcel.logo = R.drawable.sponsor_snooth;
		parcel.apiId = SnoothServices.API_ID;
		
		return parcel;

	}
	
	public static SponsorParcel createVintankSponsor(){
		SponsorParcel parcel = new SponsorParcel();
		parcel.name = "VinTank";
		parcel.url = "http://www.vintank.com";
		parcel.logo = R.drawable.sponsor_vintank;
		parcel.apiId = VinTankServices.API_ID;
		
		return parcel;

	}
}
