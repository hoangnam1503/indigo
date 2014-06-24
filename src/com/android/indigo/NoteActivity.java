package com.android.indigo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.indigo.base.FragmentActivityBase;

public class NoteActivity extends FragmentActivityBase {
	TextView aNoteBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		aNoteBtn = (TextView) findViewById(R.id.activity_note_btn);
		aNoteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(NoteActivity.this, "POST NOTE", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
