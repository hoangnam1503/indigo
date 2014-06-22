package com.android.indigo.fragment.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.indigo.R;
import com.android.indigo.custom.IndigoListView;
import com.android.indigo.custom.IndigoListView.Callbacks;

public class IndigoListFragmentBase extends ListFragment implements Callbacks {

	private IndigoListView mListView;
	private TextView mTaskView;
	private int mTaskViewHeight;
	private int mScrollY;
	
	private float mMaxRawY = 0;
	private float mTranslationY = 0;
	private ScrollSettledHandler mScrollSettledHandler = new ScrollSettledHandler();
	private int mTaskViewInitHeight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_footer, null);
		mTaskView = (TextView) view.findViewById(R.id.footer);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView = (IndigoListView) getListView();
		mListView.setCallbacks(this);

		mTaskView.getBackground().setAlpha(128);
		
		String[] array = new String[] { "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android", "Android", "Android",
				"Android", "Android", "Android" };

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, R.id.text1, array));

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
//						mQuickReturnHeight = mQuickReturnView.getHeight();
						mTaskViewHeight = mTaskView.getHeight();
						mListView.computeScrollY();
						mTaskViewInitHeight = mListView.getHeight() - mTaskViewHeight;
//						Log.e("JFF", mListView.getListHeight() + "~" + mListView.computeVerticalScrollRange() + "~" + mTaskViewInitHeight + "~" + mTaskViewHeight);
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
//				int translationY = 0;

				if (mListView.scrollIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

//				int rawY = mTaskViewInitHeight + mScrollY;
////				
//				if (mMaxRawY == 0) {
//					mMaxRawY = mTaskViewInitHeight;
//				}
////				
//				if (mTranslationY == 0) {
//					mTranslationY = mTaskViewInitHeight;
//				}
//				
//				mTranslationY += rawY - mMaxRawY;
//				if (mTranslationY > mListView.getHeight()) {
//					mTranslationY = mListView.getHeight();
//				} else if (mTranslationY < mTaskViewInitHeight) {
//					mTranslationY = mTaskViewInitHeight;
//				}
//				mMaxRawY = rawY;
//				mTaskView.animate().cancel();
//				mTaskView.setTranslationY(mTranslationY - mTaskViewInitHeight);
				onScrollChanged(mScrollY);
/*
				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					translationY = (rawY - mMinRawY) + mQuickReturnHeight;

					System.out.println(translationY);
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				*//** this can be used if the build is below honeycomb **//*
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}
*/
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	@Override
	public void onScrollChanged(int scrollY) {
//		scrollY = Math.min(mMaxScrollY, scrollY);
		mScrollSettledHandler.onScroll(scrollY);
		int rawY = mTaskViewInitHeight + scrollY;
		
		if (mMaxRawY == 0) {
			mMaxRawY = mTaskViewInitHeight;
		}
		
		if (mTranslationY == 0) {
			mTranslationY = mTaskViewInitHeight;
		}
		
		mTranslationY += rawY - mMaxRawY;
		if (mTranslationY > mListView.getHeight()) {
			mTranslationY = mListView.getHeight();
		} else if (mTranslationY < mTaskViewInitHeight) {
			mTranslationY = mTaskViewInitHeight;
		}
		mMaxRawY = rawY;
		mTaskView.animate().cancel();
		mTaskView.setTranslationY(mTranslationY - mTaskViewInitHeight);
	}

	@Override
	public void onDownMotionEvent() {
		mScrollSettledHandler.setSettleEnable(false);
	}

	@Override
	public void onUpOrCancelMotionEvent() {
		mScrollSettledHandler.setSettleEnable(true);
		mScrollSettledHandler.onScroll(mListView.getScrollY());
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
				
				if (mTaskView.getTranslationY() - mSettleScrollY < mAverageHeight) {
					float tmpY = mTaskView.getTranslationY() - mSettleScrollY - mTaskViewInitHeight;
					mDestTranslationY = mTaskView.getTranslationY() - tmpY;
					mTranslationY -= tmpY;
				} else {
					float tmpY = mListView.getHeight() + mSettleScrollY - mTaskView.getTranslationY();
					mDestTranslationY = mTaskView.getTranslationY() + tmpY;
					mTranslationY += tmpY;
				}
				mTaskView.animate().translationY(mDestTranslationY - mTaskViewInitHeight);
			}
			mSettleScrollY = Integer.MIN_VALUE;
		}
	}
}