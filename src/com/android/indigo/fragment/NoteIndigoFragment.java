package com.android.indigo.fragment;

import java.util.ArrayList;

import android.os.Bundle;

import com.android.indigo.adapter.NoteListAdapter;
import com.android.indigo.entity.Note;
import com.android.indigo.fragment.base.IndigoListFragmentBase;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class NoteIndigoFragment extends IndigoListFragmentBase {
	private NoteListAdapter mNoteListAdapter;
	private ArrayList<Note> mNoteList;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mNoteList = new ArrayList<Note>();
		mNoteListAdapter = new NoteListAdapter(mContext, mNoteList);
		SwingBottomInAnimationAdapter mSwingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mNoteListAdapter);
		mSwingBottomInAnimationAdapter.setInitialDelayMillis(300);
		mSwingBottomInAnimationAdapter.setAbsListView(mListView);
		
		mListView.setAdapter(mSwingBottomInAnimationAdapter);
		mNoteList.addAll(db.getAllNotes());
		mNoteListAdapter.notifyDataSetChanged();
	}
}
