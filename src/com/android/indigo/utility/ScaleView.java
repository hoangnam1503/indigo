package com.android.indigo.utility;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class ScaleView extends View implements OnTouchListener {
	private long mDraggingStarted;
	private float mDragStartY;
	
	private float mPointerOffset;
	private int mLastContentSize;
	
	final static private int SINGLE_TAP_MAX_TIME = 100;
	final static private int TAP_DRIFT_TOLERANCE = 1;
	
	public ScaleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLastContentSize = getContentSize();
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mDraggingStarted = SystemClock.elapsedRealtime();
			mDragStartY = event.getY();
			mPointerOffset = event.getRawY() - v.getMeasuredHeight();
			
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mDragStartY < (event.getY() + TAP_DRIFT_TOLERANCE) &&
					mDragStartY > (event.getY() - TAP_DRIFT_TOLERANCE) &&
					((SystemClock.elapsedRealtime() - mDraggingStarted) < SINGLE_TAP_MAX_TIME)) {
				setContentSize(mLastContentSize);
			}
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			setContentSize((int)(event.getY() - mPointerOffset));
		}
		return true;
	}
	
	private int getContentSize() {
		return getMeasuredHeight();
	}

	private boolean setContentSize(int newSize) {
		return setContentHeight(newSize);
	}

	private boolean setContentHeight(int newHeight) {
		ViewGroup.LayoutParams params = getLayoutParams();
		if (newHeight >= 0) {
			params.height = newHeight;
		}
		setLayoutParams(params);
		return true;
	}
	
	
}
