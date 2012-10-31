package com.yno.wizard.controller;

import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.model.service.TrackingService;
import com.yno.wizard.model.service.fb.FbListenerFactory.FbRequestListener;

import android.content.Context;
import android.os.Bundle;

public class TrackSelectCommand {

	public Bundle payload = new Bundle();
	

	
	public void execute(){
		WineParcel wine = payload.getParcelable(WineParcel.NAME);
		String phrase = payload.getString("phrase");
		TrackingService svc = new TrackingService();
		
		svc.sendSelect(wine, phrase);
	}
}
