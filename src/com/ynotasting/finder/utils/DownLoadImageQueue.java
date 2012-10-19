package com.ynotasting.finder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ynotasting.finder.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class DownLoadImageQueue {
	private static List<QueuedAsyncDownloadImage> _queue = new ArrayList<QueuedAsyncDownloadImage>();
	private static Boolean _inProcess = false;
	private static class QueuedAsyncDownloadImage extends AsyncTask<Object, Object, Object> {

		private static final String TAG = DownLoadImageQueue.class.getSimpleName();
		private ImageView iv;
		private HttpURLConnection connection;
		private InputStream is;
		private Bitmap bitmap;
		
		public String url;
		
		public QueuedAsyncDownloadImage(ImageView mImageView, String $url) {
		    iv = mImageView;
		    url = $url;
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
		    	Log.i(TAG, "no image found for location '" + params[0] + "'");
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
		    }else {
		        iv.setBackgroundResource(R.drawable.default_label);
		    }
		    
		    Animation anim = AnimationUtils.loadAnimation(iv.getContext(), R.animator.image_fadein);
		    iv.startAnimation(anim);
		    

		    QueuedAsyncDownloadImage async = _queue.remove(0);
		    
		    if( _queue.size()==0 )
		    	_inProcess = false;
		    else{
		    	async = _queue.get(0);
		    	async.execute( async.url );
		    }
		    
		}
	}

	
	
	public Boolean isInProcess(){
		return _inProcess;
	}
	
	public void addToQueue( ImageView $iv, String $url ){
		if( $url.equals("") ){
			$iv.setBackgroundResource(R.drawable.default_label);
		}else{
			QueuedAsyncDownloadImage async = new QueuedAsyncDownloadImage($iv, $url);
			
			_queue.add( async );
			
			if( !_inProcess ){
				_inProcess = true;
				async.execute(async.url);
			}
		}
			
	}
	
}
