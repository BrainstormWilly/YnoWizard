package com.yno.wizard.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.yno.wizard.R;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.view.assist.WineSubnavAssist;

public class WineReviewRatingFragment extends Fragment {

	public static final String TAG = WineReviewRatingFragment.class.getSimpleName();
	private WineSubnavAssist _helper;
	
	public static WineReviewRatingFragment newInstance( FbWineReviewParcel $review, ArrayList<String> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(FbWineReviewParcel.NAME, $review);
		arg.putStringArrayList("subnav", $subnav);
		
		WineReviewRatingFragment frag = new WineReviewRatingFragment();
		frag.setArguments( arg );
		return frag;
	}
	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_review_rate, $container, false);
		final SeekBar ratingSB = (SeekBar) view.findViewById(R.id.wineReviewRateSB);
		final TextView ratingTV = (TextView) view.findViewById(R.id.wineReviewRateTV);
		RadioGroup ratingRG = (RadioGroup) view.findViewById(R.id.wineReviewRateRG);
		
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavAssist(view);
		_helper.setNav(subnav, WineReviewActivity.NAV_RATING);
		
		final FbWineReviewParcel review = getArguments().getParcelable( FbWineReviewParcel.NAME );
		
		ratingRG.setOnCheckedChangeListener(
				new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if( checkedId==R.id.wineReviewRate100RB )
							review.setMax(100);
						else if( checkedId==R.id.wineReviewRate20RB )
							review.setMax(20);
						else
							review.setMax(5);
						
						//ratingSB.setMax(review.getMax()*10);
						ratingTV.setText(review.getFormattedValue());
					}
				}
		);
		
		ratingSB.setOnSeekBarChangeListener(
			new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					float prc = new Float(progress)/100;
					review.value = new Double(prc*review.getMax());
					ratingTV.setText( review.getFormattedValue() );
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
