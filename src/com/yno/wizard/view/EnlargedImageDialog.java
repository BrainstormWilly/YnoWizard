package com.yno.wizard.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.yno.wizard.R;
import com.yno.wizard.utils.AsyncDownloadImage;

public class EnlargedImageDialog extends Dialog implements OnClickListener {

	private final Context _context;
	private Button _btn;
	private ImageView _iv;
	
	public EnlargedImageDialog( Context $context ){
		super( $context );
		setContentView(R.layout.wine_image_enlarger);
		_context = $context;
		
		_btn = (Button) findViewById(R.id.wineImageEnlargeBtn);
		_iv = (ImageView) findViewById(R.id.wineImageEnlargeIV);
		
		_btn.setOnClickListener( this );
	}
	
	public void setImage( String $url ){
		new AsyncDownloadImage( _iv ).execute( $url );
	}
	
	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
