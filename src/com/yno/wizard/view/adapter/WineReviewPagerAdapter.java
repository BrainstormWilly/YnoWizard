package com.yno.wizard.view.adapter;

import java.util.ArrayList;

import com.yno.wizard.R;
import com.yno.wizard.model.fb.FbWineReviewParcel;
import com.yno.wizard.view.WineReviewCommentFragment;
import com.yno.wizard.view.WineReviewRatingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WineReviewPagerAdapter extends FragmentStatePagerAdapter {

	private FbWineReviewParcel _review;
	private ArrayList<Integer> _subnav = new ArrayList<Integer>();
	
	public WineReviewPagerAdapter( FragmentManager $fm, FbWineReviewParcel $review ){
		super( $fm );
		_review = $review;
		_subnav.add(R.string.review_pager_subtitle_comments);
		_subnav.add(R.string.review_pager_subtitle_ratings);
	}
	
	@Override
	public Fragment getItem( int $index ){
		if( $index==0 )
			return WineReviewCommentFragment.newInstance(_review, _subnav);
		return WineReviewRatingFragment.newInstance(_review, _subnav);
	}
	
	@Override
	public int getCount(){
		return _subnav.size();
	}

}
