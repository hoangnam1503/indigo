package com.android.indigo.fragment.base;

import java.util.ArrayList;

import com.android.indigo.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragmentBase extends ListFragment {
	protected View mView;
	protected Context mContext;
	protected ListView mListView;
	protected ArrayList<Integer> mArrayList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
		mView = layoutInflater.inflate(R.layout.fragment_list, container, false);
		
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView = (ListView) getListView();
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
}
