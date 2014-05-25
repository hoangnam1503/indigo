package com.android.indigo.adapter;

import com.android.indigo.fragment.NoteIndigoFragment;
import com.android.indigo.fragment.TodoIndigoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
			return new TodoIndigoFragment();
		default:
			return new NoteIndigoFragment();
		}
	}

	@Override
	public int getCount() {
		return mTabsTitle.length;
	}
	
}
