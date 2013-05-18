package com.example.citywalkapplayout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
		
		Bundle b = getIntent().getExtras();
		
		String tourTitleString = b.getString("tourTitle");
		String tourDescriptionString = b.getString("tourDescription");
		
		tourTitle.setText(tourTitleString);
		tourTitle.setTextColor(Color.parseColor("#FFFFFF"));
		
		tourDescription.setText(tourDescriptionString);
		tourDescription.setTextColor(Color.parseColor("#FFFFFF"));
		
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

	public void backTourButton(){
		Intent start = new Intent(this, StartActivity.class);
		startActivity(start);
	}
	
	public void startTourButton(){
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
