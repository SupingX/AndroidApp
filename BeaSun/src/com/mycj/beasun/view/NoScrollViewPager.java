package com.mycj.beasun.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可设置是否可滑动的ViewPager
 * 默认不可滑动
 * @author Administrator
 *
 */
public class NoScrollViewPager extends ViewPager {
	/**
	 * 是否可滑动
	 */
	private boolean canScroll = false;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (canScroll == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (canScroll == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public boolean isCanScroll() {
		return canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

}
