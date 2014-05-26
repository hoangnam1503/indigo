package com.android.indigo.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.indigo.R;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;

public class TodoListAdapter extends ExpandableListItemAdapter<Integer> {
	private Context mContext;
	
	public TodoListAdapter(Context context, List<Integer> listItem) {
		super(context, R.layout.adapter_todolist, R.id.todo_title, R.id.todo_content, listItem);
		this.mContext = context;
	}

	/*
	private class ViewHolder {
		TextView tvDate;
		TextView tvTitle;
		TextView tvContent;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_nodelist, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.note_title_text);
			viewHolder.tvDate = (TextView) view.findViewById(R.id.note_title_text);
			viewHolder.tvContent = (TextView) view.findViewById(R.id.note_content_text);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(calendar.getTime());
		
		viewHolder.tvTitle.setText("This is card " + (getItem(position)));
		viewHolder.tvDate.setText(date);
		viewHolder.tvContent.setText("This\n is \n Just4Fun's\n property");
		
		return view;
	}
	*/
	
	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		if (textView == null) {
			textView = new TextView(mContext);
		}
		textView.setText("Card No." + position);
		return textView;
	}

	@Override
	public View getTitleView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		if (textView == null) {
			textView = new TextView(mContext);
		}
		textView.setText("This\n is \n Just4Fun's\n property");
		return textView;
	}
}
