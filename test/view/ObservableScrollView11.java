package com.android.indigo.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView11 extends ScrollView {
	public static interface Callback {
		public void onScrollChanged(int ScrollY);
		public void onMotionEventDown();
		public void onMotionEventUp();
	}
	
	private Callback mCallback;
	
	public ObservableScrollView11(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		if (mCallback != null ) {
			mCallback.onScrollChanged(t);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mCallback != null) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mCallback.onMotionEventDown();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mCallback.onMotionEventUp();
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	
	public void setCallback(Callback listener) {
		this.mCallback = listener;
	}
}
