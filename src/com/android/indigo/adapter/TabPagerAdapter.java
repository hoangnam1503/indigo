package com.android.indigo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.indigo.fragment.base.IndigoListFragmentBase;
import com.android.indigo.fragment.base.ObservableFragmentBase;

public class TabPagerAdapter extends FragmentPagerAdapter {
	private final String[] mTabsTitle = { "Todo", "Note" };
	
	public TabPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTabsTitle[position];
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new IndigoListFragmentBase();
		default:
			return new ObservableFragmentBase();
		}
	}

	@Override
	public int getCount() {
		return mTabsTitle.length;
	}
	
}
