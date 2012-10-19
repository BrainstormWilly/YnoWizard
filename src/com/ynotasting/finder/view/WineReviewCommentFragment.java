package com.ynotasting.finder.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ynotasting.finder.R;
import com.ynotasting.finder.model.fb.FbWineReviewParcel;
import com.ynotasting.finder.utils.WineSubnavHelper;

public class WineReviewCommentFragment extends Fragment {

	public static final String TAG = WineReviewCommentFragment.class.getSimpleName();
	private WineSubnavHelper _helper;
	
	public static WineReviewCommentFragment newInstance( FbWineReviewParcel $review, ArrayList<String> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(FbWineReviewParcel.NAME, $review);
		arg.putStringArrayList("subnav", $subnav);
		
		WineReviewCommentFragment frag = new WineReviewCommentFragment();
		frag.setArguments( arg );
		return frag;
	}
	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_review_comments, $container, false);
		EditText commentsET = (EditText) view.findViewById(R.id.wineReviewCommentsET);
		
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavHelper(view);
		_helper.setNav(subnav, WineReviewActivity.NAV_COMMENTS);
		
		final FbWineReviewParcel review = getArguments().getParcelable( FbWineReviewParcel.NAME );
		commentsET.setHint("i.e. I just tasted " + review.wine.name + "...");
		
		commentsET.addTextChangedListener(
			new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					review.description = s.toString();
				}
			}
		);
		
		return view;
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		_helper.reset();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		_helper.resume();
	}
}
