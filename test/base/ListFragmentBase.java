package com.android.indigo.fragment.base;

import java.util.ArrayList;

import android.content.Context;
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
import android.widget.TextView;

import com.android.indigo.R;
import com.android.indigo.custom.IndigoListView;
import com.android.indigo.custom.IndigoListView.Callbacks;

public class ListFragmentBase extends ListFragment implements Callbacks {
	protected View mView;
	protected Context mContext;
	protected IndigoListView mListView;
	protected ArrayList<Integer> mArrayList;

	private TextView mTaskView;
	private float mMaxRawY = 0;
	private float mTranslationY = 0;
	private int mTaskViewInitHeight;
	private int mScrollY;
	private ScrollSettledHandler mScrollSettledHandler = new ScrollSettledHandler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
		mView = layoutInflater.inflate(R.layout.fragment_list, container, false);
		mTaskView = (TextView) mView.findViewById(R.id.sticky);
		
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView = (IndigoListView) getListView();
		mListView.setCallbacks(this);

		mTaskView.setText(R.string.quick_return_item);
		mTaskView.getBackground().setAlpha(128);

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mListView.computeScrollY();
						mTaskViewInitHeight = mListView.getHeight() - mTaskView.getHeight();
					}
				});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mScrollY = 0;
				if (mListView.scrollIsComputed()) {
					mScrollY = mListView.getComputedScrollY() + 32;
				}

//				mScrollY = Math.min(mMaxScrollY, mScrollY);
				mScrollSettledHandler.onScroll(mScrollY);
				int rawY = mTaskViewInitHeight + mScrollY;
				
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
//				mTaskView.animate().cancel();
//				mTaskView.setTranslationY(mTranslationY + mScrollY);
				mTaskView.setTranslationY(mTranslationY);
//				onScrollChanged(mScrollY);
			}
		});
	}
	
	public Object getItem(int position) {
		return mArrayList.get(position);
	}
	
	protected ArrayList<Integer> getItems() {
		mArrayList = new ArrayList<Integer>();
		for (int i = 0; i< 10; i++) {
			mArrayList.add(i);
		}
		
		return mArrayList;
	}

	@Override
	public void onScrollChanged(int scrollY) {
//		Log.e("TEST", "" + scrollY);
//		scrollY = Math.min(mMaxScrollY, scrollY);
//		mScrollSettledHandler.onScroll(scrollY);
//		int rawY = mTaskViewInitHeight + scrollY;
//		
//		if (mMaxRawY == 0) {
//			mMaxRawY = mTaskViewInitHeight;
//		}
//		
//		if (mTranslationY == 0) {
//			mTranslationY = mTaskViewInitHeight;
//		}
//		
//		mTranslationY += rawY - mMaxRawY;
//		if (mTranslationY > mListView.getHeight()) {
//			mTranslationY = mListView.getHeight();
//		} else if (mTranslationY < mTaskViewInitHeight) {
//			mTranslationY = mTaskViewInitHeight;
//		}
//		mMaxRawY = rawY;
//		mTaskView.animate().cancel();
//		mTaskView.setTranslationY(mTranslationY + scrollY);
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
					mTranslationY -= mTaskView.getTranslationY() - mSettleScrollY - mTaskViewInitHeight;
				} else {
					float tmpY = mListView.getHeight() + mSettleScrollY - mTaskView.getTranslationY();
					mDestTranslationY = mTaskView.getTranslationY() + tmpY;
					mTranslationY += mListView.getHeight() - mTaskView.getTranslationY() + mSettleScrollY;
				}
				mTaskView.animate().translationY(mDestTranslationY);
			}
			mSettleScrollY = Integer.MIN_VALUE;
		}
	}
}
