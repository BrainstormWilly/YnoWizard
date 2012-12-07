package com.yno.wizard.view.adapter;

import java.util.ArrayList;

import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.WineSelectActivity;
import com.yno.wizard.view.WineSelectDescripFragment;
import com.yno.wizard.view.WineSelectDetailsFragment;
import com.yno.wizard.view.WineSelectNotesFragment;
import com.yno.wizard.view.WineSelectPricesFragment;
import com.yno.wizard.view.WineSelectRatingsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WineSelectPagerAdapter extends FragmentStatePagerAdapter {

	private WineParcel _wine;
	private ArrayList<String> _subnav;
	
	
	public WineSelectPagerAdapter( FragmentManager $fm, WineParcel $wine, ArrayList<String> $subnav ){
		super( $fm );
		_wine = $wine;
		_subnav = $subnav;
		
	}
	
	@Override
	public Fragment getItem( int $index ){
		if( _subnav.get($index).equals(WineSelectActivity.NAV_DESCRIPTION) )
			return WineSelectDescripFragment.newInstance(_wine, _subnav);
		else if( _subnav.get($index).equals(WineSelectActivity.NAV_PRICES) )
			return WineSelectPricesFragment.newInstance(_wine, _subnav);
		else if( _subnav.get($index).equals(WineSelectActivity.NAV_RATINGS) )
			return WineSelectRatingsFragment.newInstance(_wine, _subnav);
		else if( _subnav.get($index).equals(WineSelectActivity.NAV_NOTES) )
			return WineSelectNotesFragment.newInstance(_wine, _subnav);
		
		return WineSelectDetailsFragment.newInstance(_wine, _subnav);
	}
	
	@Override
	public int getCount(){
		return _subnav.size();
	}

}
