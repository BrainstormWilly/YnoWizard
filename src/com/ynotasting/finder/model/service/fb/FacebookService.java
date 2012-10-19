package com.ynotasting.finder.model.service.fb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.ynotasting.finder.R;
import com.ynotasting.finder.YnoFinderApplication;
import com.ynotasting.finder.model.fb.FbModelFactory;
import com.ynotasting.finder.model.fb.FbUserParcel;
import com.ynotasting.finder.model.fb.FbWineReviewParcel;
import com.ynotasting.finder.view.IFacebookContext;

public class FacebookService {

	public static final String TAG = FacebookService.class.getSimpleName();
	
	public static String APP_ID = "440134882684670";
	public static String[] PERMISSIONS = { "publish_actions", "user_birthday" };
	public static String USER_PARAMS = "first_name, last_name, gender, birthday";
	public static String ACTION_TASTE = "me/ynowizard:taste";
	
	private static final String _TOKEN = "access_token";
	private static final String _EXPIRES = "access_expires";
	private static final String _KEY = "facebook-session";
	private static final String _USER = "me";
	private static final String _FEED = "feed";
	private static final String _ACTIVITY = "https://graph.facebook.com/me/activity/";
	
	private static final String _RUNNER_FIELDS = "fields";
	
	
	private IFacebookContext _context;
	private YnoFinderApplication _app;
	private SharedPreferences _prefs;
	
	
	public FacebookService( IFacebookContext $context ){
		
		_context = $context;
		_app = YnoFinderApplication.getInstance();
		
		Log.d(TAG, "Facebook app is null? " + (_app.FB==null));
		
		if( _app.FB==null ){
			_app.FB = new Facebook(APP_ID);
			_app.FB_RUNNER = new AsyncFacebookRunner(_app.FB);
		}
		
		_prefs = getContext().getSharedPreferences(_KEY, Context.MODE_PRIVATE);
		String token = _prefs.getString(_TOKEN, null);
		long expires = _prefs.getLong(_EXPIRES, 0);
		Log.d(TAG, "create token = " + token);
		if( token!=null )
			_app.FB.setAccessToken(token);
		if( expires!=0 )
			_app.FB.setAccessExpires(expires);
	}
	
	public void authorize( int $ssoCode ){
		Log.d(TAG, "authorize in process");
		_app.FB.authorize( getActivity(), PERMISSIONS, $ssoCode, 
				new FbListenerFactory.FbDialogListener(){
					@Override
					public void onCancel() {
						Log.d(TAG, "authorize cancel");
						_context.onServiceCancel("authorize");
					}
					@Override
					public void onComplete(Bundle values) {
						Editor editor = _prefs.edit();
						Log.d(TAG, "authorize token = " + _app.FB.getAccessToken());
						editor.putString(_TOKEN, _app.FB.getAccessToken());
						editor.putLong(_EXPIRES, _app.FB.getAccessExpires());
						editor.commit();
						_context.onDialogComplete(values, "authorize");
					}
					@Override
					public void onError(DialogError e) {
						Log.d(TAG, "authorize onError");
						_context.onDialogError(e, "authorize");
					}
					@Override
					public void onFacebookError(FacebookError e) {
						Log.d(TAG, "authorize onFacebookError");
						_context.onFacebookError(e, "authorize");
					}
				}
		);
	}
	
	public void authorizeCallback(int $request, int $result, Intent $intent){
		_app.FB.authorizeCallback($request, $result, $intent);
	}
	
	public void clearSession() {
        Editor editor = _prefs.edit();
        editor.clear();
        editor.commit();
    }
	
	public Activity getActivity(){
		return (Activity) _context;
	}
	
	public Context getContext(){
		return (Context) _context;
	}
	
	public void extendAccessTokenIfNeeded(){
		_app.FB.extendAccessTokenIfNeeded(getContext(), null);
	}
	
	public void getUserData(){
		Bundle params = new Bundle();
		params.putString(_RUNNER_FIELDS, USER_PARAMS);
		_app.FB_RUNNER.request(_USER, params, 
				new FbListenerFactory.FbRequestListener(){
					@Override
					public void onComplete(String response, Object state) {
						_context.onRequestComplete(  FbModelFactory.createUser(response), "getUserData" );
					}
					@Override
					public void onFacebookError(FacebookError e, Object state) {
						_context.onFacebookError(e, "getUserData");
					}
					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						_context.onServiceException(e, "getUserData");
					}
					@Override
					public void onIOException(IOException e, Object state) {
						_context.onServiceException(e, "getUserData");
					}
					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						_context.onServiceException(e, "getUserData");
					}
				}
		);
		
	}
	
	public void publishReviewToList( FbWineReviewParcel $review ){
		String wineURL = getContext().getString( R.string.url_fb_redirect )
				+ "?title=" + URLEncoder.encode($review.wine.nameQualified)
				+ "&description=" + URLEncoder.encode($review.wine.description);
		if($review.wine.imageLarge.equals(""))
			wineURL += "&image=" + getContext().getString( R.string.url_default_label );
		else
			wineURL += "&image=" + $review.wine.imageLarge;
		
		//Log.d(TAG, wineURL );
		Bundle params = new Bundle();
		params.putString("wine", wineURL);
		params.putString("comments", $review.description);
		params.putString("rating_string", $review.getRatingString());
		params.putString("rating_value", String.valueOf($review.getWeightedValue()) );

		_app.FB_RUNNER.request(ACTION_TASTE, params, "POST",
				new FbListenerFactory.FbRequestListener(){
					@Override
					public void onComplete(String response, Object state) {
						//Log.d(TAG, "response = " + response );
						_context.onRequestComplete( response, "publishReviewToList"  );
					}
					@Override
					public void onFacebookError(FacebookError e, Object state) {
						_context.onFacebookError(e, "publishReviewToList");
					}
					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						_context.onServiceException(e, "publishReviewToList");
					}
					@Override
					public void onIOException(IOException e, Object state) {
						_context.onServiceException(e, "publishReviewToList");
					}
					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						_context.onServiceException(e, "publishReviewToList");
					}
				}, "state"
		);
	}
	
	public void publishReviewToFeed( FbWineReviewParcel $review ){
		Bundle params = new Bundle();
		params.putString("app_id", APP_ID);
		params.putString("link", _ACTIVITY + $review.id);
		params.putString("name", $review.wine.nameQualified);
		params.putString("caption", $review.getRatingString());
		params.putString("description", $review.description);
		if($review.wine.imageLarge.equals(""))
			params.putString("picture", getContext().getString( R.string.url_default_label ) );
		else
			params.putString("picture", $review.wine.imageLarge);
		_app.FB.dialog(getContext(), _FEED, params,
			new FbListenerFactory.FbDialogListener(){
				@Override
				public void onCancel() {
					_context.onServiceCancel("publishReviewToFeed");
				}
				
				@Override
				public void onComplete(Bundle values) {
					_context.onDialogComplete(values, "publishReviewToFeed");
				}
				
				@Override
				public void onError(DialogError e) {
					_context.onDialogError(e, "publishReviewToFeed");
				}
				
				@Override
				public void onFacebookError(FacebookError e) {
					_context.onFacebookError(e, "publishReviewToFeed");
				}
			}
		
		);
	}
	
	public boolean isSessionValid(){
		return _app.FB.isSessionValid();
	}
	
	public boolean restoreSession(){
		SharedPreferences savedSession = getContext().getSharedPreferences(_KEY, Context.MODE_PRIVATE);
        _app.FB.setAccessToken(savedSession.getString(_TOKEN, null));
        _app.FB.setAccessExpires(savedSession.getLong(_EXPIRES, 0));
        return isSessionValid();
	}
	
	public boolean saveSession() {
        Editor editor = getContext().getSharedPreferences(_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(_TOKEN, _app.FB.getAccessToken());
        editor.putLong(_EXPIRES, _app.FB.getAccessExpires());
        return editor.commit();
    }
	
	public void unauthorize(){
		AsyncFacebookRunner runner = new AsyncFacebookRunner( _app.FB );
		runner.logout(getContext(), 
				new FbListenerFactory.FbRequestListener(){
					@Override
					public void onComplete(String response, Object state) {
						FbUserParcel parcel = new FbUserParcel();
						_context.onRequestComplete(parcel, "unauthorize");
					}
					@Override
					public void onFacebookError(FacebookError e, Object state) {
						_context.onFacebookError(e, "unauthorize");
					}
					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						// TODO Auto-generated method stub
						_context.onServiceException(e, "unauthorize");
					}
					@Override
					public void onIOException(IOException e, Object state) {
						// TODO Auto-generated method stub
						_context.onServiceException(e, "unauthorize");
					}
					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						// TODO Auto-generated method stub
						_context.onServiceException(e, "unauthorize");
					}
				}
		);
	}

}
