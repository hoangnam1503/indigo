package com.android.indigo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class IndigoListView extends ListView {
    private Callbacks mCallbacks;
    private int mItemCount;
    private int mItemOffsetY[];
    private boolean scrollIsComputed = false;
    private int mHeight;
	
    public IndigoListView(Context context) {
    	super(context);
    }
    
	public IndigoListView (Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public int getListHeight() {
		return mHeight;
	}
	
	public void computeScrollY() {
		mHeight = 0;
		mItemCount = getAdapter().getCount();
		if (mItemOffsetY == null) {
			mItemOffsetY = new int[mItemCount];
		}
		
		for (int i = 0; i < mItemCount; ++i) {
			View view = getAdapter().getView(i, null, this);
			view.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
			);
			mItemOffsetY[i] = mHeight;
			mHeight += view.getMeasuredHeight();
		}
		scrollIsComputed = true;
	}
	
	public boolean scrollIsComputed() {
		return scrollIsComputed;
	}

	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = view.getTop();
		nScrollY = mItemOffsetY[pos] - nItemY;
		
		return nScrollY;
	}
/*
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
        }
    }
*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (mCallbacks != null) {
    		switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					mCallbacks.onDownMotionEvent();
					Log.e("test", "down");
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					Log.e("test", "up");
					mCallbacks.onUpOrCancelMotionEvent();
					break;
			}
    	}
    	return super.onTouchEvent(event);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public static interface Callbacks {
        public void onScrollChanged(int scrollY);
        public void onDownMotionEvent();
        public void onUpOrCancelMotionEvent();
    }
}
