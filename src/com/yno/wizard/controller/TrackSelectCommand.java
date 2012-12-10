package com.yno.wizard.controller;

import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.TrackingService;
import com.yno.wizard.model.service.fb.FbListenerFactory.FbRequestListener;

import android.content.Context;
import android.os.Bundle;

public class TrackSelectCommand {

	public Bundle payload = new Bundle();
	

	
	public void execute(){
		SearchWineParcel parcel = payload.getParcelable(SearchWineParcel.NAME);
		TrackingService svc = new TrackingService();
		
		svc.sendSelect(parcel);
	}
}
