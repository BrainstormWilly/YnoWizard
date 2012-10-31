package com.yno.wizard.controller;

import android.os.Bundle;

import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.model.service.TrackingService;

public class TrackReviewCommand {

	public Bundle payload = new Bundle();
	
	
	
	public void execute(){
		FbWineReviewParcel review = payload.getParcelable(FbWineReviewParcel.NAME);
		FbUserParcel user = payload.getParcelable(FbUserParcel.NAME);
		TrackingService svc = new TrackingService();
		
		svc.sendReview(review, user);
	}
}
