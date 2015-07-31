package com.app.laundry.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class HomeTabViewPager extends ViewPager{

	public HomeTabViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if(v != this && v instanceof ViewPager) {
	        int currentItem = ((ViewPager) v).getCurrentItem();
	        int countItem = ((ViewPager) v).getAdapter().getCount();
	        if((currentItem==(countItem-1) && dx<0) || (currentItem==0 && dx>0)){
	            return false;
	        }
	        return true;
	    }
	    return super.canScroll(v, checkV, dx, x, y);
	}
}
