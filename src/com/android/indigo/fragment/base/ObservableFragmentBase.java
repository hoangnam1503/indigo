package com.android.indigo.fragment.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.android.indigo.R;
import com.android.indigo.utility.ObservableScrollView;

public class ObservableFragmentBase extends Fragment implements ObservableScrollView.Callbacks  {
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	
	private TextView mTaskView;
	private ObservableScrollView mObservableScrollView;
	private int mMaxRawY = 0;
	private int mState = STATE_ONSCREEN;
	private int mTaskViewInitHeight;
	private int mMaxScrollY;
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
		scrollY = Math.min(mMaxScrollY, scrollY);
		mScrollSettledHandler.onScroll(scrollY);
		
		int rawY = mTaskViewInitHeight + scrollY;
		int translationY = 0;
		
		switch (mState) {
			case STATE_OFFSCREEN:
				if (rawY >= mMaxRawY) {
					mMaxRawY = rawY;
				} else {
					mState = STATE_RETURNING;
				}
				translationY = rawY;
				break;
			case STATE_ONSCREEN:
				if (rawY > mObservableScrollView.getHeight()) {
					mState = STATE_OFFSCREEN;
					mMaxRawY = rawY;
				}
				translationY = rawY;
				break;
			case STATE_RETURNING:
				translationY = mObservableScrollView.getHeight() - (mMaxRawY - rawY);
				
				// while mMaxRawY - rawY = mTaskView.getHeight() then the translationY is fix to mTaskViewHeight
				if (translationY < mTaskViewInitHeight) {
					translationY = mTaskViewInitHeight;
				}
				
				// the animation is executed until the taskview is fully showing
				if (rawY <= mTaskViewInitHeight) {
					mState = STATE_ONSCREEN;
					translationY = rawY;
				}
				
				// while the user reverse the swipe direct, it turn back to the offscreen_state
				if (rawY > mMaxRawY) {
					mState = STATE_OFFSCREEN;
					mMaxRawY = rawY;
				}
				break;
		}
		
		mTaskView.animate().cancel();
		mTaskView.setTranslationY(translationY + scrollY);
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
				int mDestTranslationY = Integer.MIN_VALUE;
				if (mSettleScrollY > mTaskView.getHeight()) {
						if (mState == STATE_RETURNING) {
							int mAverageHeight = mTaskViewInitHeight + mTaskView.getHeight() / 2;
							
							if (mTaskView.getTranslationY() - mSettleScrollY > mAverageHeight) {
								mState = STATE_OFFSCREEN;
								mDestTranslationY = mObservableScrollView.getHeight() + mSettleScrollY;
								mMaxRawY = mTaskViewInitHeight + mSettleScrollY;
							} else {
								mDestTranslationY = mTaskViewInitHeight + mSettleScrollY;
								mMaxRawY = mObservableScrollView.getHeight() + mSettleScrollY;
							}
						}
				} else {
					if (mSettleScrollY > mTaskView.getHeight() / 2) {
						mDestTranslationY = mObservableScrollView.getHeight() + mSettleScrollY;
					} else {
						mDestTranslationY = mTaskViewInitHeight + mSettleScrollY;
					}
					mMaxRawY = mDestTranslationY;
				}
				mTaskView.animate().translationY(mDestTranslationY);
			}
			mSettleScrollY = Integer.MIN_VALUE;
		}
	}
}
