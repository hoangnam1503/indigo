package com.android.indigo.fragment;

import android.os.Bundle;
import com.android.indigo.adapter.NoteListAdapter;
import com.android.indigo.fragment.base.ListFragmentBase;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class NoteIndigoFragment extends ListFragmentBase {
	private NoteListAdapter mNoteListAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mNoteListAdapter = new NoteListAdapter(mContext);
		SwingBottomInAnimationAdapter mSwingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mNoteListAdapter);
		mSwingBottomInAnimationAdapter.setInitialDelayMillis(300);
		mSwingBottomInAnimationAdapter.setAbsListView(mListView);
		
		mListView.setAdapter(mSwingBottomInAnimationAdapter);
		mNoteListAdapter.addAll(getItems());
	}
}
