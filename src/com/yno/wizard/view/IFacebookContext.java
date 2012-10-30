package com.yno.wizard.view;

import android.os.Bundle;
import android.os.Parcelable;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;

public interface IFacebookContext {

	void onDialogComplete( Bundle $data, String $service );
	void onDialogError( DialogError $e, String $service );
	void onRequestComplete( Parcelable $obj, String $service );
	void onRequestComplete( String $reponse, String $service );
	void onFacebookError( FacebookError $e, String $service );
	void onServiceException( Exception $e, String $service );
	void onServiceCancel( String $service );
	
}
