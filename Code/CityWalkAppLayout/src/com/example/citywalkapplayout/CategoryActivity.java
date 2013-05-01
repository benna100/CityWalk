package com.example.citywalkapplayout;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends ListActivity {

	static final String[] categories2 = new String[] { "Browse walks" ,"Architectural", "Inspirational" , "Scenic", "Romantic", "Historical", "Shopping" , "Entertaining"  };
	private ArrayList<String> categories = new ArrayList();

    private LinearLayout ll;
    private TextView tv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new CategoryAdapter(this, categories2));
		getWindow().getDecorView().setBackgroundColor(Color.BLACK);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
 
	}

}
