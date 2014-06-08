package com.android.indigo;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.indigo.adapter.TabPagerAdapter;
import com.android.indigo.base.FragmentActivityBase;
import com.android.indigo.dialog.NoteDialog;
import com.android.indigo.dialog.base.DialogFragmentBase.DialogListener;
import com.android.indigo.utility.SlidingTabStrip;

public class HomeActivity extends FragmentActivityBase implements DialogListener {
	private DrawerLayout mDrawerLayout;
	private PopupWindow mPopupWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		mNoteDialog = new NoteDialog();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.homeDrawer);
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int i) {}
			
			@Override
			public void onDrawerSlide(View view, float f) {}
			
			@Override
			public void onDrawerOpened(View arg0) {
				// Invalidate the activity menu, fully update via onCreateOptionsMenu and onPrepareOptionsMenu at the next time
				supportInvalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				supportInvalidateOptionsMenu();
			}
		});
		
		mTabStrip = (SlidingTabStrip) findViewById(R.id.homeTabStrip);
		mViewPager = (ViewPager) findViewById(R.id.homeViewPaper);
		mTabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		
		mTabStrip.setShouldExpand(true);
		mTabStrip.setIndicatorColorResource(R.color.indicator_slider_color);
		
		mViewPager.setAdapter(mTabAdapter);
		mTabStrip.setViewPager(mViewPager);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		boolean drawer = mDrawerLayout.isDrawerVisible(Gravity.RIGHT);
		
		menu.findItem(R.id.action_setting).setVisible(!drawer);
		menu.findItem(R.id.action_done).setVisible(drawer);
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "You're home now!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_note:
//			Intent intent = new Intent(HomeActivity.this, TestActivity.class);
//			goNextActivity(intent);
			onShowDialog();
			return true;
		case R.id.action_setting:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			return true;
		case R.id.action_done:
			mDrawerLayout.closeDrawers();
			return true;
		}
		
		return super.onOptionsItemSelected(menuItem);
	}
	
	public void onShowPopup(View view) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final View inflateView = layoutInflater.inflate(R.layout.dialog_note, null, false);
		
		Display display = getWindowManager().getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		
		mPopupWindow = new PopupWindow(inflateView, size.x -200, size.y - 200);
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dialog_popup));
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 100);
	}
	
	public void onShowDialog() {
		if (!mNoteDialog.isAdded()) {
			getSupportFragmentManager().beginTransaction().add(mNoteDialog, "Note").commitAllowingStateLoss();
		}
	}

	@Override
	public void onClose() {
		getSupportFragmentManager().beginTransaction().remove(mNoteDialog).commitAllowingStateLoss();
	}
}
