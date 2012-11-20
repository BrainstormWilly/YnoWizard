package com.yno.wizard.view;

import android.util.Log;

import com.yno.wizard.model.service.AsyncServiceParcel;
import com.yno.wizard.model.service.IServiceContext;
import com.yno.wizard.model.service.PhraseServiceParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.VinTankAsyncService;
import com.yno.wizard.utils.TextSearchACAdapter;

public class TextSearchFieldServiceHelper implements IServiceContext {
	
	public static final String TAG = "TextSearchFieldServiceHelper";

	private TextSearchACAdapter _ctx;
	
	public TextSearchFieldServiceHelper( TextSearchACAdapter $ctx ){
		_ctx = $ctx;
	}
	
	public void begin( CharSequence $phrase ){
		new VinTankAsyncService( this ).getWineNamesByQuery( $phrase.toString(), 1, SearchData.AUTOCOMPLETE_TOTAL );
	}
	
	@Override
	public void resume( AsyncServiceParcel $parcel ) {
		PhraseServiceParcel parcel = (PhraseServiceParcel) $parcel;
		Log.d(TAG, "resume");
	}

}
