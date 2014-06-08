package com.android.indigo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class IndigoView extends View {
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	private Context mContext;

	public IndigoView(Context context) {
		super(context, null);
		this.mContext = context;
		mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
	}

	public IndigoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		
		canvas.restore();
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			
			invalidate();
			return true;
		}
	}
}
