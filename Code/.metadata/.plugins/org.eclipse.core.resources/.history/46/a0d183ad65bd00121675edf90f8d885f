package com.example.citywalkapplayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class NoteInfo extends Activity {

	TextView noteTitle;
	TextView noteDescription;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_info);
		Bundle b = getIntent().getExtras();
		
		String noteTitleString = b.getString("noteTitle");
		String notDescriptionString = b.getString("noteDescription");
		
		noteTitle = (TextView) findViewById(R.id.noteTitle);
		noteTitle.setText(noteTitleString);
		
		noteDescription = (TextView) findViewById(R.id.noteDescription);
		noteDescription.setText(notDescriptionString);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_info, menu);
		return true;
	}

}