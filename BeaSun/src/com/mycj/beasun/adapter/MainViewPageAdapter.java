package com.mycj.beasun.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainViewPageAdapter extends FragmentStatePagerAdapter {
	
	private List<Fragment> fragments; 
	public MainViewPageAdapter(FragmentManager fm) {
		super(fm);
	}
	public MainViewPageAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments =fragments;
	}
	
	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;//返回此情况 ViewPager 会重新创建
	}

}
