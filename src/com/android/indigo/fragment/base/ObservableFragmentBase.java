package com.android.indigo.fragment.base;

import android.os.Bundle;
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
				
				if (translationY < mTaskViewInitHeight) {
					translationY = mTaskViewInitHeight;
				}
				
				if (rawY <= mTaskViewInitHeight) {
					mState = STATE_ONSCREEN;
					translationY = rawY;
				}
				
				if (rawY > mMaxRawY) {
					mState = STATE_OFFSCREEN;
					mMaxRawY = rawY;
				}
				break;
		}
		
		mTaskView.animate().cancel();
		mTaskView.setTranslationY(translationY + scrollY);
	}
}
