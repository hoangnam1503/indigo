package com.android.indigo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.indigo.fragment.base.ObservableFragmentBase;
import com.android.indigo.utility.QuickReturnFragmentBase;

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
//			return new TodoIndigoFragment();
			return new QuickReturnFragmentBase();
		default:
			return new ObservableFragmentBase();
//			return new QuickReturnFragmentBase();
		}
	}

	@Override
	public int getCount() {
		return mTabsTitle.length;
	}
	
}
