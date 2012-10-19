package com.ynotasting.finder.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncUploadImage extends AsyncTask<Bitmap, Integer, String> {
	
	public static final String TAG = AsyncUploadImage.class.getSimpleName();

	private Context _context;
	private String _filename;
	
	private long totalSize;
	private ProgressDialog _pd;
	
	public AsyncUploadImage(Context $context, String $filename){
		super();
		_context = $context;
		_filename = $filename;
	}
	
	@Override
	protected void onPreExecute()
	{
		_pd = new ProgressDialog(_context);
		_pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		_pd.setMessage("Uploading Picture...");
		_pd.setCancelable(false);
		_pd.show();
	}
	
	@Override
	protected String doInBackground(Bitmap... $args) {
		try {
			Bitmap bmp = $args[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("http://brnstorm.com/yno/uploader.php");
			
			ProgressMultiPartEntity entity = new ProgressMultiPartEntity(
					new ProgressMultiPartEntity.ProgressListener(){
						@Override
						public void transferred(long num){
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					}
			);
			totalSize = entity.getContentLength();
			

//			MultipartEntity entity = new MultipartEntity(
//					HttpMultipartMode.BROWSER_COMPATIBLE);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();
			entity.addPart("Filedata", new ByteArrayBody(data, _filename));
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							response.getEntity().getContent(), "UTF-8"));

			String sResponse = reader.readLine();
			Log.d(TAG, "response = " + sResponse);
			return sResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// (null);
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		_pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String $response) {
		Log.d(TAG, "onPostExecute: " + $response);
		try{
			if (_pd.isShowing())
				_pd.dismiss();
		}catch(IllegalArgumentException $e){
			Toast.makeText(_context.getApplicationContext(), "Image upload complete", Toast.LENGTH_SHORT);
			$e.printStackTrace();
		}
//		try {
//			
//
//			if (sResponse != null) {
//				JSONObject JResponse = new JSONObject(sResponse);
//				int success = JResponse.getInt("SUCCESS");
//				String message = JResponse.getString("MESSAGE");
//				if (success == 0) {
//					Toast.makeText(getApplicationContext(), message,
//							Toast.LENGTH_LONG).show();
//				} else {
//					Toast.makeText(getApplicationContext(),
//							"Photo uploaded successfully",
//							Toast.LENGTH_SHORT).show();
//					caption.setText("");
//				}
//			}
//		} catch (Exception e) {
//			Toast.makeText(getApplicationContext(),
//					getString(R.string.exception_message),
//					Toast.LENGTH_LONG).show();
//			Log.e(e.getClass().getName(), e.getMessage(), e);
//		}
	}
}
