package com.yno.wizard.view.assist;

import java.util.ArrayList;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yno.wizard.R;

public class WineSubnavAssist {
	
	private View _view;
	private View _leftArwVw;
	private View _rightArwVw;
	private Boolean _leftOn = false;
	private Boolean _rightOn = false;
	private Animation _anim;
	
	public WineSubnavAssist(View $view){
		_view = $view;
		_leftArwVw = (View) $view.findViewById(R.id.wineFragmentArrowLeftVw);
		_rightArwVw = (View) $view.findViewById(R.id.wineFragmentArrowRightVw);
		_anim = AnimationUtils.loadAnimation(_view.getContext(), R.animator.fragment_arrow_fadein);
	}

	public void setNav(ArrayList<String> $subnav, String $head){
		
		TextView prevTV = (TextView) _view.findViewById(R.id.wineSelectSubnavPrevTV);
		TextView currTV = (TextView) _view.findViewById(R.id.wineSelectSubnavCurrTV);
		TextView nextTV = (TextView) _view.findViewById(R.id.wineSelectSubnavNextTV);
		
		for( int a=0, l=$subnav.size(); a<l; a++ ){
			if( $subnav.get(a).equals($head) ){
				
				prevTV.setText("");
				nextTV.setText("");
				currTV.setText($subnav.get(a));
				if( a>0 ){
					_leftOn = true;
					_leftArwVw.setVisibility(View.VISIBLE);
					_leftArwVw.startAnimation(_anim);
					prevTV.setText($subnav.get(a-1));
				}
				if( a<l-1 ) {
					_rightOn = true;
					_rightArwVw.setVisibility(View.VISIBLE);
					_rightArwVw.startAnimation(_anim);
					nextTV.setText($subnav.get(a+1));
				}
				break;
			}
		}
	}
	
	public void reset(){
		_leftArwVw.setVisibility(View.GONE);
		_leftArwVw.setVisibility(View.GONE);
	}
	
	public void resume(){
		if( _leftOn ){
			_leftArwVw.setVisibility(View.VISIBLE);
			_leftArwVw.startAnimation(_anim);
		}
		if( _rightOn ){
			_rightArwVw.setVisibility(View.VISIBLE);
			_rightArwVw.startAnimation(_anim);
		}
	}
}
