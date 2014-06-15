
package com.android.indigo.fragment.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.android.indigo.R;
import com.android.indigo.utility.ObservableScrollView;

public class ObservableFragmentBase extends Fragment implements ObservableScrollView.Callbacks  {
	
	private TextView mTaskView;
	private ObservableScrollView mObservableScrollView;
	private float mMaxRawY = 0;
	private float mTranslationY = 0;
	private int mTaskViewInitHeight;
	private int mMaxScrollY;
	private int mState = 0;
	private ScrollSettledHandler mScrollSettledHandler = new ScrollSettledHandler();
	
	public ObservableFragmentBase() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_content, container, false);
		
		mObservableScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll_view);
		mObservableScrollView.setCallbacks(this);
		
		mTaskView = (TextView) rootView.findViewById(R.id.sticky);
		mTaskView.setText(R.string.quick_return_item);
		mTaskView.getBackground().setAlpha(128);
		
		mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScrollChanged(mObservableScrollView.getScrollY());
						mMaxScrollY = mObservableScrollView.computeVerticalScrollRange() - mObservableScrollView.getHeight();
						mTaskViewInitHeight = mObservableScrollView.getHeight() - mTaskView.getHeight();
					}
				});
		
		return rootView;
	}

	@Override
	public void onScrollChanged(int scrollY) {
//		Log.e("test", "" + mTaskView.getTranslationY());
		scrollY = Math.min(mMaxScrollY, scrollY);
		mScrollSettledHandler.onScroll(scrollY);
		int rawY = mTaskViewInitHeight + scrollY;
		
		if (mMaxRawY == 0) {
			mMaxRawY = mTaskViewInitHeight;
		}
		
		if (mTranslationY == 0) {
			mTranslationY = mTaskViewInitHeight;
		}
		
		mTranslationY += rawY - mMaxRawY;
		if (mTranslationY > mObservableScrollView.getHeight()) {
			mTranslationY = mObservableScrollView.getHeight();
		} else if (mTranslationY < mTaskViewInitHeight) {
			Log.e("test", "" + mTranslationY);
			mTranslationY = mTaskViewInitHeight;
		}
		mMaxRawY = rawY;
//		Log.e("TEST", "test " + mMaxRawY);
		mTaskView.animate().cancel();
		mTaskView.setTranslationY(mTranslationY + scrollY);
	}
	
	@Override
	public void onDownMotionEvent() {
		mScrollSettledHandler.setSettleEnable(false);
	}
	
	@Override
	public void onUpOrCancelMotionEvent() {
		mScrollSettledHandler.setSettleEnable(true);
		mScrollSettledHandler.onScroll(mObservableScrollView.getScrollY());
	}
	
	private class ScrollSettledHandler extends Handler {
		private static final int SETTLE_DELAY_MILIS = 3000;
		
		private int mSettleScrollY = Integer.MIN_VALUE;
		private boolean mSettleEnable = false;
		
		public void onScroll(int scrollY) {
			if (mSettleScrollY != scrollY) {
				removeMessages(0);
				sendEmptyMessageAtTime(0, SETTLE_DELAY_MILIS);
				mSettleScrollY = scrollY;
			}
		}
		
		public void setSettleEnable(boolean settleEnabled) {
			this.mSettleEnable = settleEnabled;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (mSettleEnable) {
				float mDestTranslationY;
				int mAverageHeight = mTaskViewInitHeight + mTaskView.getHeight() / 2;
				int i = 0;
				
				if (mTaskView.getTranslationY() - mSettleScrollY < mAverageHeight) {
					float tmpY = mTaskView.getTranslationY() - mSettleScrollY - mTaskViewInitHeight;
//					mDestTranslationY = mTaskViewInitHeight + mSettleScrollY;
					mDestTranslationY = mTaskView.getTranslationY() - tmpY;
					mMaxRawY = mDestTranslationY;
					i = 1;
				} else {
					float tmpY = mObservableScrollView.getHeight() + mSettleScrollY - mTaskView.getTranslationY();
//					mDestTranslationY = mObservableScrollView.getHeight() + mSettleScrollY;
					mDestTranslationY = mTaskView.getTranslationY() + tmpY;
					mMaxRawY = mDestTranslationY;
					i = 2;
				}
//				Log.e("test", "test " + i + "~" + mMaxRawY);
				mTaskView.animate().translationY(mDestTranslationY);
			}
			mSettleScrollY = Integer.MIN_VALUE;
		}
	}
}
