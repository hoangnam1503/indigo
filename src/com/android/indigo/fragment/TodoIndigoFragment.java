package com.android.indigo.fragment;

import android.os.Bundle;

import com.android.indigo.adapter.TodoListAdapter;
import com.android.indigo.fragment.base.ListFragmentBase;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

public class TodoIndigoFragment extends ListFragmentBase {
	private TodoListAdapter mTodoListAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mTodoListAdapter = new TodoListAdapter(mContext, getItems());
		AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mTodoListAdapter);
		mAlphaInAnimationAdapter.setAbsListView(mListView);
		mAlphaInAnimationAdapter.setInitialDelayMillis(500);
		
		mListView.setAdapter(mAlphaInAnimationAdapter);
	}
}
