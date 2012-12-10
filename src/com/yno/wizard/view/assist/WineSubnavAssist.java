package com.yno.wizard.view.assist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yno.wizard.R;

public class WineSubnavAssist {
	
	private WeakReference<View> _view;
	//private WeakReference<View> _leftArwVw;
	//private WeakReference<View> _rightArwVw;
	private Boolean _leftOn = false;
	private Boolean _rightOn = false;
	private Animation _anim;
	
	public WineSubnavAssist(View $view){
		_view = new WeakReference<View>($view);
		//_leftArwVw = new WeakReference<View>( $view.findViewById(R.id.wineFragmentArrowLeftVw) );
		//_rightArwVw = new WeakReference<View>( $view.findViewById(R.id.wineFragmentArrowRightVw) );
		_anim = AnimationUtils.loadAnimation(_view.get().getContext(), R.animator.fragment_arrow_fadein);
	}

	public void setNav(ArrayList<Integer> $subnav, int $head){
		
		View vw = _view.get();
		if( vw==null )
			return;
		
		TextView prevTV = (TextView) vw.findViewById(R.id.wineSelectSubnavPrevTV);
		TextView currTV = (TextView) vw.findViewById(R.id.wineSelectSubnavCurrTV);
		TextView nextTV = (TextView) vw.findViewById(R.id.wineSelectSubnavNextTV);
		View lftArwVw = (View) vw.findViewById(R.id.wineFragmentArrowLeftVw);
		View rgtArwVw = (View) vw.findViewById(R.id.wineFragmentArrowRightVw);
		
		for( int a=0, l=$subnav.size(); a<l; a++ ){
			if( $subnav.get(a).equals($head) ){
				
				prevTV.setText("");
				nextTV.setText("");
				currTV.setText($subnav.get(a));
				if( a>0 ){
					_leftOn = true;
					lftArwVw.setVisibility(View.VISIBLE);
					lftArwVw.startAnimation(_anim);
					prevTV.setText($subnav.get(a-1));
				}
				if( a<l-1 ) {
					_rightOn = true;
					rgtArwVw.setVisibility(View.VISIBLE);
					rgtArwVw.startAnimation(_anim);
					nextTV.setText($subnav.get(a+1));
				}
				break;
			}
		}
	}
	
	public void reset(){
		View vw = _view.get();
		if( vw==null )
			return;
		
		View lftArwVw = (View) vw.findViewById(R.id.wineFragmentArrowLeftVw);
		View rgtArwVw = (View) vw.findViewById(R.id.wineFragmentArrowRightVw);
		lftArwVw.setVisibility(View.GONE);
		rgtArwVw.setVisibility(View.GONE);
	}
	
	public void resume(){
		View vw = _view.get();
		if( vw==null )
			return;
		
		View lftArwVw = (View) vw.findViewById(R.id.wineFragmentArrowLeftVw);
		View rgtArwVw = (View) vw.findViewById(R.id.wineFragmentArrowRightVw);
		if( _leftOn ){
			lftArwVw.setVisibility(View.VISIBLE);
			lftArwVw.startAnimation(_anim);
		}
		if( _rightOn ){
			rgtArwVw.setVisibility(View.VISIBLE);
			rgtArwVw.startAnimation(_anim);
		}
	}
}
