package com.example.citywalkapplayout;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryActivity extends Activity {

	private ArrayList<String> categories = new ArrayList();

    private LinearLayout ll;
    private TextView tv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		categories.add("title1");
		setContentView(R.layout.activity_category);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.info);
		int counter = 1;
		for (String title : categories) {
			TextView valueTV = new TextView(this);
		    valueTV.setText("hallo hallo");
		    valueTV.setId(counter);
		    valueTV.setLayoutParams(new LayoutParams(
		            LayoutParams.WRAP_CONTENT,
		            LayoutParams.WRAP_CONTENT));
		    linearLayout.addView(valueTV);
		    ImageView imageView = new ImageView(this);
		    
		    counter ++;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}

}
