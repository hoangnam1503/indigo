package com.android.indigo.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.indigo.R;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;

public class TodoListAdapter extends ExpandableListItemAdapter<Integer> {
	private Context mContext;
	
	public TodoListAdapter(Context context, List<Integer> listItem) {
		super(context, R.layout.adapter_todolist, R.id.todo_title, R.id.todo_content, listItem);
		this.mContext = context;
	}

	private class TitleViewHolder {
		TextView tvDate;
		TextView tvTitle;
	}
	
	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		if (textView == null) {
			textView = new TextView(mContext);
		}
		textView.setText("This\n is \n Just4Fun's\n property");
		return textView;
	}

	@Override
	public View getTitleView(int position, View convertView, ViewGroup parent) {
		TitleViewHolder titleViewHolder;
		View view = (View) convertView;
		
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.adapter_todolist_title, parent, false);
			
			titleViewHolder = new TitleViewHolder();
			titleViewHolder.tvTitle = (TextView) view.findViewById(R.id.todo_title_text);
			titleViewHolder.tvDate = (TextView) view.findViewById(R.id.todo_date_text);
			
			view.setTag(titleViewHolder);
		} else {
			titleViewHolder = (TitleViewHolder) view.getTag();
		}
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(calendar.getTime());
		
		titleViewHolder.tvTitle.setText("Card No." + position);
		titleViewHolder.tvDate.setText(date);
		
		return view;
	}
}
