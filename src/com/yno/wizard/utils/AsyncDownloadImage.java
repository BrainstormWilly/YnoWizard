package com.yno.wizard.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yno.wizard.R;

public class AsyncDownloadImage extends AsyncTask<Object, Object, Object> {

	public static final String TAG = "AsyncDownloadImage ";
	ImageView iv;
	private HttpURLConnection connection;
	private InputStream is;
	private Bitmap bitmap;
	
	public AsyncDownloadImage(ImageView mImageView) {
	    iv = mImageView;
	}
	@Override
	protected Object doInBackground(Object... params) {
	    URL url;
	    try {
	        url = new URL((String) params[0]);
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        is = connection.getInputStream();
	        bitmap = BitmapFactory.decodeStream(is);
	        is.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (is != null) {
	                is.close();
	            }
	            if (connection != null) {
	                connection.disconnect();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return bitmap;
	}
	@Override
	protected void onPostExecute(Object result) {
	    super.onPostExecute(result);
	    
	    if (null != result) {
	        iv.setImageBitmap((Bitmap) result);
	        //Log.i(TAG, "image download ok！！！");
	    }else {
	        iv.setBackgroundResource(R.drawable.default_label);
	    }
	    Animation anim = AnimationUtils.loadAnimation(iv.getContext(), R.animator.image_fadein);
	    iv.startAnimation(anim);
	}
	
}
