package com.android.indigo;

import java.util.Date;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.indigo.base.FragmentActivityBase;
import com.android.indigo.entity.Note;
import com.android.indigo.helper.IndigoSQLiteHelper;

public class NoteActivity extends FragmentActivityBase {
	TextView aNoteBtn;
	EditText aNoteTitle, aNoteContent;
	IndigoSQLiteHelper db = new IndigoSQLiteHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		aNoteTitle = (EditText) findViewById(R.id.activity_note_title);
		aNoteContent = (EditText) findViewById(R.id.activity_note_content);
		
		aNoteBtn = (TextView) findViewById(R.id.activity_note_btn);
		aNoteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date now = new Date();
				
				Note note = new Note();
				note.setTitle(aNoteTitle.getText().toString());
				note.setContent(aNoteContent.getText().toString());
				note.setDate(now.toString());
				
				db.saveNote(note);
			}
		});
	}
}
