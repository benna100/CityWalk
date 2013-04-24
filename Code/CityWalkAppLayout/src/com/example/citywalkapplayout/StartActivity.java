package com.example.citywalkapplayout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		PreviewTour tour1 = new PreviewTour("Sight","300m","30min",R.drawable.lakes);
		PreviewTour tour2 = new PreviewTour("Sight2","600m","60min",R.drawable.mermaid);
		PreviewTour tour3 = new PreviewTour("Sight2","600m","60min",R.drawable.mermaid);
		PreviewTour tour4 = new PreviewTour("Sight","300m","30min",R.drawable.lakes);
		PreviewTour[] tours = {tour1,tour2,tour3};
		
		ListView list = (ListView) findViewById(R.id.list);
		
		list.setAdapter(new CityWalkTourAdapter(this, tours));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}
}