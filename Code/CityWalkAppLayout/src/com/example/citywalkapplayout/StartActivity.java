package com.example.citywalkapplayout;

import java.util.List;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class StartActivity extends Activity {
	
	public static Tour selected;
	float x = 0;
	float y = 0;
	int pos = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
//		final Tour[] tours = {new Tour(),new Tour(),new Tour(),new Tour()};
//		
//		for(int i = 0; i < tours.length; i++){
//			Tour t = tours[i];
//			t.setTitle("tour "+ i);
//			t.setImg(R.drawable.mermaid);
//			t.setDistance("30km");
//			t.setDuration(90);new Tour()new Tour()new Tour()
//		}
		
		ServerAccessLayer server = new ServerAccessLayer();
		final List<Tour> tourList = server.getSortedTour("views");
		final Tour[] tours = new Tour[tourList.size()];
		for(int i = 0;i < tourList.size(); i++){
			Tour t = tourList.get(i);
			t.setImg(R.drawable.mermaid);
			t.setDistance(300);
			tours[i] = t;
		}
		
		ListView listView = (ListView) findViewById(R.id.list);
		
		final CityWalkTourAdapter adapter = new CityWalkTourAdapter(this, tours);
		
		listView.setAdapter(adapter);
		
		listView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					ListView layout = (ListView)v;
					int x = (int) event.getX();
					int y = (int) event.getY();
					int pos;
					int un = 0;
					boolean uneven = false;

				        for(int i =0; i< layout.getChildCount(); i++)
				        {
				            View view = layout.getChildAt(i);


				            Rect outRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

				            if(outRect.contains(x, y))
				            {
				            	pos = (i+1)*2-(i+1);
				            	if(tours.length+1 > pos){
					            	if(tours.length % 2 == 1){
					            		un = tours.length-1;
					            		uneven = true;
					            	}
					            	
					            	int w = view.getWidth();
				            		if(x < w/2 | pos>un & uneven){
				            			pos -= 1;
				            		}
					            	
					            	selected = tours[pos];
					            	select(selected);
				  		    	  	adapter.notifyDataSetChanged();
					            }
				            }
				        }
				    
	            }
	            return true;
			}
		});
//		
////		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////		      @Override
////		      public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
////		    	  final PreviewTour t;
////		    	  try {
////		    		  t = (PreviewTour) parent.getItemAtPosition(position+pos);
////			    	  t.setTitle("" + parent.getCount());
////				} catch (Exception e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////		    	  adapter.notifyDataSetChanged();
////		      }
////
////		 });
	}
	
	public void select(Tour tour){
		Intent start = new Intent(this, TourInfo.class);
		String title = tour.title;
		String description = tour.description;
		Bundle b1 = new Bundle();
		b1.putString("tourTitle", title);
		b1.putString("tourDescription", description);
		start.putExtras(b1);
		startActivity(start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}
}