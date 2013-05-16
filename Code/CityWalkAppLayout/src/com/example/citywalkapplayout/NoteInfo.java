package com.example.citywalkapplayout;

import com.google.android.gms.plus.model.people.Person.Image;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;

public class NoteInfo extends Activity {

TextView noteTitle;
TextView noteDescription;
Button nextNote;
Button noteInfoBackButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_info);
		Bundle b = getIntent().getExtras();
		
		String noteTitleString = b.getString("noteTitle");
		String notDescriptionString = b.getString("noteDescription");
		
		nextNote = (Button)findViewById(R.id.nextNote);
		noteInfoBackButton = (Button)findViewById(R.id.noteInfoBackButton);
		nextNote.setText("Next sight");
		nextNote.setTextColor(Color.parseColor("#FFFFFF"));
		noteInfoBackButton.setTextColor(Color.parseColor("#FFFFFF"));
		noteInfoBackButton.setText("Back to map");
		
		
		noteTitle = (TextView) findViewById(R.id.noteTitle);
		noteTitle.setText(noteTitleString);
		noteTitle.setTextColor(Color.parseColor("#FFFFFF"));
		
		//noteDescription = (TextView) findViewById(R.id.noteDescription);
		//noteDescription.setText(notDescriptionString);
		//noteDescription.setTextColor(Color.parseColor("#FFFFFF"));
		
		
		Drawable dIcon = getResources().getDrawable(R.drawable.mermaid);
        
		//Drawable dIcon = getResources().getDrawable(R.drawable.noteImage);
        
		int leftMargin = dIcon.getIntrinsicWidth()+70;

        ImageView icon = (ImageView) findViewById(R.id.noteImage);
        icon.setBackgroundDrawable(dIcon);

        SpannableString ss = new SpannableString("asd ka skd aks dkj kjas dk askd jas kd kas dkjas dkj askjd kjas dkj aksjd kjas dkja skjd akjs dkjas dk askd kas dk askjd aksj dkjas dkja skdj aksjd kjasdnjas k");
        //SpannableString ss = new SpannableString(notDescriptionString);
        ss.setSpan(new MyLeadingMarginSpan2(3, leftMargin), 0, ss.length(), 0);

        noteDescription = (TextView) findViewById(R.id.noteDescription);
        noteDescription.setTextColor(Color.parseColor("#FFFFFF"));
        noteDescription.setTextSize(16);
        noteDescription.setWidth(400);
        noteDescription.setText(ss);
		
		noteInfoBackButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	backToMap();
		    }
		});
		
		nextNote.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	nextNote();
		    }
		});
		
		
	}
	
	public void backToMap(){
		Intent start = new Intent(this, GoogleMapActivity.class);
		startActivity(start);
	}
	
	public void nextNote(){
		Intent start = new Intent(this, GoogleMapActivity.class);
		startActivity(start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_info, menu);
		return true;
	}

}