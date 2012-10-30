package com.yno.wizard.model.service.fb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.ServiceListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;



public class FbListenerFactory {

	
	public static interface IAuthListener {

        /**
         * Called when a auth flow completes successfully and a valid OAuth
         * Token was received. Executed by the thread that initiated the
         * authentication. API requests can now be made.
         */
        public void onAuthSucceed();

        /**
         * Called when a login completes unsuccessfully with an error.
         * 
         * Executed by the thread that initiated the authentication.
         */
        public void onAuthFail(String error);
    }
	
	/**
     * Callback interface for logout events.
     */
    public static interface ILogoutListener {
        /**
         * Called when logout begins, before session is invalidated. Last chance
         * to make an API call. Executed by the thread that initiated the
         * logout.
         */
        public void onLogoutBegin();

        /**
         * Called when the session information has been cleared. UI should be
         * updated to reflect logged-out state.
         * 
         * Executed by the thread that initiated the logout.
         */
        public void onLogoutFinish();
    }
    
    public static class FbAPIsAuthListener implements IAuthListener {

        @Override
        public void onAuthSucceed() {
            // stub
        	
        }

        @Override
        public void onAuthFail(String error) {
            // stub
        }
    }
    
    public static class FbDialogListener implements DialogListener {
    	
    	@Override
    	public void onCancel() {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onComplete(Bundle values) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onError(DialogError e) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onFacebookError(FacebookError e) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    public static class FbRequestListener implements RequestListener{
    	
    	@Override
    	public void onComplete(String response, Object state) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onFacebookError(FacebookError e, Object state) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onFileNotFoundException(FileNotFoundException e,
    			Object state) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onIOException(IOException e, Object state) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onMalformedURLException(MalformedURLException e,
    			Object state) {
    		// TODO Auto-generated method stub
    		
    	}
    }
    
    public static class FbServiceListener implements ServiceListener{
    	
    	@Override
    	public void onComplete(Bundle values) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onError(Error e) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	@Override
    	public void onFacebookError(FacebookError e) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    }
    
    
}
