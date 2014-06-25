package com.android.indigo.fragment.base;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.indigo.NoteActivity;
import com.android.indigo.R;
import com.android.indigo.custom.IndigoListView;
import com.android.indigo.custom.IndigoListView.Callbacks;
import com.android.indigo.helper.IndigoSQLiteHelper;

public class IndigoListFragmentBase extends ListFragment implements Callbacks {
	
	protected Context mContext;
	protected ArrayList<Integer> mArrayList;
	protected IndigoSQLiteHelper db;

	protected IndigoListView mListView;
	private TextView mTaskView;
	private int mTaskViewHeight;
	private int mScrollY;
	
	private float mMaxRawY = 0;
	private float mTranslationY = 0;
	private ScrollSettledHandler mScrollSettledHandler = new ScrollSettledHandler();
	private int mTaskViewInitHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		this.db = new IndigoSQLiteHelper(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, null);
		mTaskView = (TextView) view.findViewById(R.id.footer);
		mTaskView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), NoteActivity.class);
				startActivity(intent);
			}
		});
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
						mTaskViewHeight = mTaskView.getHeight();
						mListView.computeScrollY();
						mTaskViewInitHeight = mListView.getHeight() - mTaskViewHeight;
					}
				});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				if (mListView.scrollIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}
				
				onScrollChanged(mScrollY);

				/** this can be used if the build is below honeycomb **//*
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}*/

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	@Override
	public void onScrollChanged(int scrollY) {
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
		mTaskView.setTranslationY(mTranslationY - mTaskViewInitHeight);
	}

	@Override
	public void onDownMotionEvent() {
		mScrollSettledHandler.setSettleEnable(false);
	}

	@Override
	public void onUpOrCancelMotionEvent() {
		mScrollSettledHandler.setSettleEnable(true);
		if (mListView.scrollIsComputed()) {
			mScrollY = mListView.getComputedScrollY();
		}
		mScrollSettledHandler.onScroll(mScrollY);
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
				if (mSettleScrollY == 0) {
					mTranslationY = mTaskViewInitHeight;
				} else {
					int mAverageHeight = mTaskViewHeight / 2;
					
					if (mTaskView.getTranslationY() < mAverageHeight) {
						mTranslationY = mTaskViewInitHeight;
					} else {
						mTranslationY = mListView.getHeight();
					}
				}
				mTaskView.animate().translationY(mTranslationY - mTaskViewInitHeight);
			}
			mSettleScrollY = Integer.MIN_VALUE;
		}
	}
}