package com.yno.wizard.controller;

import android.os.Bundle;

import com.yno.wizard.model.PriceParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.model.service.TrackingService;

public class TrackBuyLinkCommand {

public Bundle payload = new Bundle();
	
	public void execute(){
		PriceParcel vendor = payload.getParcelable(PriceParcel.NAME);
		WineParcel wine = payload.getParcelable(WineParcel.NAME);
		TrackingService svc = new TrackingService();
		
		svc.sendBuyLink(vendor, wine);
	}
	
}
