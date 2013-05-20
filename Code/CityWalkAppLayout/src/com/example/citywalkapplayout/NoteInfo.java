package com.example.citywalkapplayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteInfo extends Activity {

	TextView noteTitle;
	TextView noteDescription;
	Button nextNote;
	Button noteInfoBackButton;
	private int noteNumber;
	private boolean finish;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_info);
		Bundle b = getIntent().getExtras();

		String noteTitleString = b.getString("noteTitle");
		String notDescriptionString = b.getString("noteDescription");
		String imageUrl = b.getString("imageUrl");
		noteNumber = b.getInt("noteNumber");
		//finish = b.getBoolean("finish");
		type = b.getString("type");

		nextNote = (Button) findViewById(R.id.nextNote);
		noteInfoBackButton = (Button) findViewById(R.id.noteInfoBackButton);
		nextNote.setText("Continue");
		nextNote.setTextColor(Color.parseColor("#FFFFFF"));
		noteInfoBackButton.setTextColor(Color.parseColor("#FFFFFF"));
		noteInfoBackButton.setText("Back to map");

		noteTitle = (TextView) findViewById(R.id.noteTitle);
		noteTitle.setText(noteTitleString);
		noteTitle.setTextColor(Color.parseColor("#FFFFFF"));

		// noteDescription = (TextView) findViewById(R.id.noteDescription);
		// noteDescription.setText(notDescriptionString);
		// noteDescription.setTextColor(Color.parseColor("#FFFFFF"));

		Drawable urlImage = null;
		try {
			urlImage = drawableFromUrl(imageUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("un here");
		}

		// Drawable dIcon = getResources().getDrawable(R.drawable.mermaid);

		// Drawable dIcon = getResources().getDrawable(R.drawable.noteImage);

		// int leftMargin = dIcon.getIntrinsicWidth()+70;
		int leftMargin = 320;
		ImageView icon = (ImageView) findViewById(R.id.noteImage);
		// icon.setBackgroundDrawable(dIcon);
		icon.setBackgroundDrawable(urlImage);
		// icon.setBackgroundDrawable(dIcon);

//		SpannableString ss = new SpannableString(
//				"asd ka skd aks dkj kjas dk askd jas kd kas dkjas dkj askjd kjas dkj aksjd kjas dkja skjd akjs dkjas dk askd kas dk askjd aksj dkjas dkja skdj aksjd kjasdnjas k");
		 SpannableString ss = new SpannableString(notDescriptionString);
		ss.setSpan(new MyLeadingMarginSpan2(5, leftMargin), 0, ss.length(), 0);

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

	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}

	public void backToMap() {
		Intent start = new Intent(this, GoogleMapActivity.class);

		startActivity(start);
	}

	public void nextNote() {
		if (type.contains("finish")) {
			Intent start = new Intent(this, FinishActivity.class);
			startActivity(start);	
		} else if (type.contains( "location")){
			Intent start = new Intent(this, GoogleMapActivity.class);
			Bundle b1 = new Bundle();
			
			noteNumber++;
			b1.putInt("noteNumber", noteNumber);
			b1.putInt("hasFired", noteNumber);
			start.putExtras(b1);
			startActivity(start);
		}
		else {
			Intent start = new Intent(this, GoogleMapActivity.class);
			Bundle b1 = new Bundle();
			int hasFired = noteNumber+1;
			b1.putInt("noteNumber", noteNumber);
			b1.putInt("hasFired", hasFired);
			startActivity(start);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_info, menu);
		return true;
	}

}