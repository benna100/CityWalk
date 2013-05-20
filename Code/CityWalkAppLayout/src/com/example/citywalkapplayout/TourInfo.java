package com.example.citywalkapplayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TourInfo extends Activity {

	TextView tourTitle;
	TextView tourDescription;
	Button startTourButton;
	Button backTourButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_info);

		tourTitle = (TextView) findViewById(R.id.tourTitle);
		tourDescription = (TextView) findViewById(R.id.tourDescription);
		startTourButton = (Button) findViewById(R.id.startTourButton);
		startTourButton.setText("Start guided tour");
		startTourButton.setTextColor(Color.parseColor("#FFFFFF"));
		backTourButton = (Button) findViewById(R.id.backTour);
		backTourButton.setText("Back");
		backTourButton.setTextColor(Color.parseColor("#FFFFFF"));
		ImageView icon = (ImageView) findViewById(R.id.tourImage);

		Bundle b = getIntent().getExtras();
		
		int id = b.getInt("id");
		for(int i = 0; i < StartActivity.fulllist.size();i++){
			Tour t = StartActivity.fulllist.get(i);
			if(t.getId() == id){
				icon.setBackgroundDrawable(t.getImg());
			}
		}

		String tourTitleString = b.getString("tourTitle");
		String tourDescriptionString = b.getString("tourDescription");

		tourTitle.setText(tourTitleString);
		tourTitle.setTextColor(Color.parseColor("#FFFFFF"));
		
		int leftMargin = 320;
        //icon.setBackgroundDrawable(dIcon);
        //icon.setBackgroundDrawable(dIcon);

//        SpannableString ss = new SpannableString("asd ka skd aks dkj kjas dk askd jas kd kas dkjas dkj askjd kjas dkj aksjd kjas dkja skjd akjs dkjas dk askd kas dk askjd aksj dkjas dkja skdj aksjd kjasdnjas k");
        SpannableString ss = new SpannableString(tourDescriptionString);
        ss.setSpan(new MyLeadingMarginSpan2(5, leftMargin), 0, ss.length(), 0);

		tourDescription.setTextColor(Color.parseColor("#FFFFFF"));
        tourDescription.setTextSize(16);
        tourDescription.setWidth(400);
        tourDescription.setText(ss);

		backTourButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backTourButton();
			}
		});

		startTourButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTourButton();
			}
		});
	}

//	public static Drawable drawableFromUrl(String url) throws IOException {
//		Bitmap x;
//
//		HttpURLConnection connection = (HttpURLConnection) new URL(url)
//				.openConnection();
//		connection.connect();
//		InputStream input = connection.getInputStream();
//
//		x = BitmapFactory.decodeStream(input);
//		return new BitmapDrawable(x);
//	}

	public void backTourButton() {
		Intent start = new Intent(this, StartActivity.class);
		startActivity(start);
	}

	public void startTourButton() {
		Intent start = new Intent(this, GoogleMapActivity.class);
		Bundle b1 = new Bundle();
		b1.putInt("noteNumber", 0);
		start.putExtras(b1);
		startActivity(start);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tour_info, menu);
		return true;
	}

}
