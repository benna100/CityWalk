package com.example.citywalkapplayout;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView list = (ListView) findViewById(R.id.list);
		 
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title1", "Sightseeing");
		map.put("dist1", "300m");
		map.put("time1", "30min");
		map.put("title2", "Sights");
		map.put("dist2", "2500m");
		map.put("time2", "90min");
		mylist.add(map);
		map = new HashMap<String, String>();
		map.put("title1", "Cake");
		map.put("dist1", "3m");
		map.put("time1", "1min");
		map.put("title2", "Snore!!!");
		map.put("dist2", "500m");
		map.put("time2", "120min");
		mylist.add(map);
		// ...
		SimpleAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.list_2line,
		            new String[] {"title1", "dist1", "time1", "title2", "dist2", "time2"}, new int[] {R.id.title1, R.id.dist1, R.id.time1, R.id.title2, R.id.dist2, R.id.time2});
		list.setAdapter(mSchedule);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
