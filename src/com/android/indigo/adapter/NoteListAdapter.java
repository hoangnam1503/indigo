package com.android.indigo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.indigo.R;
import com.android.indigo.entity.Note;
import com.nhaarman.listviewanimations.ArrayAdapter;

public class NoteListAdapter extends ArrayAdapter<Integer> {
	private Context mContext;
	private ArrayList<Note> noteList = null;
	
	public NoteListAdapter(Context context, ArrayList<Note> noteList) {
		this.mContext = context;
		this.noteList = noteList;
	}
	
	private class ViewHolder {
		TextView tvDate;
		TextView tvTitle;
		TextView tvContent;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		
		if(view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_nodelist, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.tvDate = (TextView) view.findViewById(R.id.note_date_text);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.note_title_text);
			viewHolder.tvContent = (TextView) view.findViewById(R.id.note_content_text);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
//		Calendar calendar = Calendar.getInstance();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//		String date = dateFormat.format(calendar.getTime());
		Note note = noteList.get(position);

		viewHolder.tvTitle.setText(note.getTitle());
		viewHolder.tvDate.setText(note.getDate());
		viewHolder.tvContent.setText(note.getContent());
		return view;
	}

}
