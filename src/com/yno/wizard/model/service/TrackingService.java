package com.yno.wizard.model.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.yno.wizard.model.PriceParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.model.fb.FbUserParcel;
import com.yno.wizard.model.fb.FbWineReviewParcel;

public class TrackingService extends AsyncTask<String, Void, String> {

	public static final String BARCODE = "is_barcode";
	public static final String TAG = "TrackingService";
	private static final String URL = "http://ynowizard.com/admin/add_data.php";
	
	private List<NameValuePair> _values;
	
	public void sendSelect( WineParcel $wine, String $phrase ){
		_values = new ArrayList<NameValuePair>(11);
		_values.add( new BasicNameValuePair("access", "ynowizard") );
		_values.add( new BasicNameValuePair("data_type", "select") );
		setWine( _values, $wine );
		_values.add( new BasicNameValuePair("sel_barcode", $phrase.equals(BARCODE) ? "true" : "false") );
		_values.add( new BasicNameValuePair("sel_phrase", $phrase.equals(BARCODE) ? "" : $phrase) );
		execute( new String[]{URL} );
	}
	
	public void sendReview( FbWineReviewParcel $review, FbUserParcel $user ){
		int gender = 0;
		_values = new ArrayList<NameValuePair>(15);
		_values.add( new BasicNameValuePair("access", "ynowizard") );
		_values.add( new BasicNameValuePair("data_type", "review") );
		setWine( _values, $review.wine );
		_values.add( new BasicNameValuePair("rev_comments", $review.description) );
		_values.add( new BasicNameValuePair("rev_rating", String.valueOf($review.value)) );
		_values.add( new BasicNameValuePair("rev_rating_text", $review.getRatingString()) );
		if( $user.gender.equals("male") )
			gender = 1;
		if( $user.gender.equals("female") )
			gender = 2;
		_values.add( new BasicNameValuePair("rev_gender", String.valueOf(gender)) );
		_values.add( new BasicNameValuePair("rev_zip", $user.location) );
		_values.add( new BasicNameValuePair("rev_age", String.valueOf($user.getAge())) );
		execute( new String[]{URL} );
	}
	
	public void sendBuyLink( PriceParcel $vendor, WineParcel $wine ){
		_values = new ArrayList<NameValuePair>(11);
		_values.add( new BasicNameValuePair("access", "ynowizard") );
		_values.add( new BasicNameValuePair("data_type", "buylink") );
		setWine( _values, $wine );
		_values.add( new BasicNameValuePair("vendor_url", $vendor.url) );
		_values.add( new BasicNameValuePair("vendor_name", $vendor.seller) );
		execute( new String[]{URL});
	}
	
	@Override
	protected String doInBackground(String... $urls) {
		String response = "";
		for (String url : $urls){
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try{
				post.setEntity( new UrlEncodedFormEntity( _values ) );
				HttpResponse execute = client.execute( post );
				response = EntityUtils.toString( execute.getEntity() );
			}catch( Exception $e ){
				response = "failed";
				$e.printStackTrace();
			}
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		//Log.d( TAG, "onPostExecute" );
	}
	
	private void setWine( List<NameValuePair> $values, WineParcel $wine ){
		$values.add( new BasicNameValuePair("wine_id", $wine.id) );
		$values.add( new BasicNameValuePair("wine_name", $wine.nameQualified) );
		$values.add( new BasicNameValuePair("wine_vintage", String.valueOf($wine.year)) );	
		$values.add( new BasicNameValuePair("wine_producer", $wine.producer) );
		$values.add( new BasicNameValuePair("wine_varietal", $wine.varietal) );
		$values.add( new BasicNameValuePair("wine_description", $wine.description) );
		$values.add( new BasicNameValuePair("wine_image", $wine.imageLarge) );
	}
	
	
}
