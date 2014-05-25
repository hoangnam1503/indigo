package com.android.indigo.base;

import com.android.indigo.adapter.TabPagerAdapter;
import com.android.indigo.utility.SlidingTabStrip;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

public class FragmentActivityBase extends ActionBarActivity {
	public static final int REQUEST_CODE_BASIC = 1;
	
	protected SlidingTabStrip mTabStrip;
	protected ViewPager mViewPager;
	protected TabPagerAdapter mTabAdapter;
	
	public void goNextActivity(Intent intent) {
		startActivityForResult(intent, REQUEST_CODE_BASIC);
	}
	
	public void goNextActivity(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode);
	}
}
