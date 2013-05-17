package com.example.citywalkapplayout;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class StartActivity extends Activity implements
		SearchView.OnQueryTextListener {

	public static Tour selected;
	float x = 0;
	float y = 0;
	int pos = 0;
	List<Tour> tours = new ArrayList<Tour>();
	List<Tour> fulllist = new ArrayList<Tour>();
	CityWalkTourAdapter adapter;

	private SearchView mSearchView;
	private TextView mStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// final Tour[] tours = {new Tour(),new Tour(),new Tour(),new Tour()};
		//
		// for(int i = 0; i < tours.length; i++){
		// Tour t = tours[i];
		// t.setTitle("tour "+ i);
		// t.setImg(R.drawable.mermaid);
		// t.setDistance("30km");
		// t.setDuration(90);new Tour()new Tour()new Tour()
		// }

		ServerAccessLayer server = new ServerAccessLayer();
		List<Tour> tourList = server.getSortedTour("views");
		for (int i = 0; i < tourList.size(); i++) {
			Tour t = tourList.get(i);
			t.setImg(R.drawable.mermaid);
			t.setDistance(300);
			tours.add(t);
			fulllist.add(t);
		}

		HorizontalScrollView sorter = (HorizontalScrollView) findViewById(R.id.sorter);
		// sorter.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN){
		// // TextView t = (TextView) v;
		// // t.setText("LOL");
		// }
		// return false;
		// }
		// });

		ListView listView = (ListView) findViewById(R.id.list);

		adapter = new CityWalkTourAdapter(this, tours);

		listView.setAdapter(adapter);

		listView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ListView layout = (ListView) v;
					int x = (int) event.getX();
					int y = (int) event.getY();
					int pos;
					int un = 0;
					boolean uneven = false;

					for (int i = 0; i < layout.getChildCount(); i++) {
						View view = layout.getChildAt(i);

						Rect outRect = new Rect(view.getLeft(), view.getTop(),
								view.getRight(), view.getBottom());

						if (outRect.contains(x, y)) {
							pos = (i + 1) * 2 - (i + 1);
							if (tours.size() + 1 > pos) {
								if (tours.size() % 2 == 1) {
									un = tours.size() - 1;
									uneven = true;
								}

								int w = view.getWidth();
								if (x < w / 2 | pos > un & uneven) {
									pos -= 1;
								}

								selected = tours.get(pos);
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
		// // listView.setOnItemClickListener(new
		// AdapterView.OnItemClickListener() {
		// //
		// // @Override
		// // public void onItemClick(AdapterView<?> parent, final View view,int
		// position, long id) {
		// // final PreviewTour t;
		// // try {
		// // t = (PreviewTour) parent.getItemAtPosition(position+pos);
		// // t.setTitle("" + parent.getCount());
		// // } catch (Exception e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// // adapter.notifyDataSetChanged();
		// // }
		// //
		// // });
	}

	public void select(Tour tour) {
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
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_start, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);

		return true;
	}

	private void setupSearchView(MenuItem searchItem) {

		if (isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}

		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// if (searchManager != null) {
		// List<SearchableInfo> searchables =
		// searchManager.getSearchablesInGlobalSearch();
		//
		// SearchableInfo info =
		// searchManager.getSearchableInfo(getComponentName());
		// for (SearchableInfo inf : searchables) {
		// if (inf.getSuggestAuthority() != null
		// && inf.getSuggestAuthority().startsWith("applications")) {
		// info = inf;
		// }
		// }
		// mSearchView.setSearchableInfo(info);
		// }

		mSearchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		List<Tour> tourList = new ArrayList<Tour>();
		for (int i = 0; i < fulllist.size(); i++) {
			Tour t = fulllist.get(i);
			if (t.getTitle().toLowerCase().contains(arg0.toLowerCase()) || t.getDescription().toLowerCase().contains(arg0.toLowerCase())) {
				tourList.add(t);
			}
		}
		// int i;
		// for(i = 0; i < tourList.size(); i++){
		// tours[i] = tourList.get(i);
		// }
		// for(int j = i; j < fulllist.length; j++){
		// tours[j]
		// }
		adapter.clear();
		adapter.addAll(tourList);
		adapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return true;
	}
}