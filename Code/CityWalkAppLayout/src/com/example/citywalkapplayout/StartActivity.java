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
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

public class StartActivity extends Activity implements
		SearchView.OnQueryTextListener {

	public static Tour selected;
	float x = 0;
	float y = 0;
	int pos = 0;
	List<Tour> tours = new ArrayList<Tour>();
	List<Tour> fulllist = new ArrayList<Tour>();
	List<Tour> filterlist = new ArrayList<Tour>();
	ServerAccessLayer server = new ServerAccessLayer();
	CityWalkTourAdapter adapter;

	private SearchView mSearchView;
	private TextView mStatusView;
	Spinner categories;

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

		List<Tour> tourList = server.getSortedTour("views");
		for (int i = 0; i < tourList.size(); i++) {
			Tour t = tourList.get(i);
			t.setDistance(300);
			tours.add(t);
			fulllist.add(t);
		}

		filterlist = fulllist;

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
		categories = (Spinner) findViewById(R.id.categories);
		categories.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String sel = parent.getSelectedItem().toString();
				categorize(sel, pos);
				// List<Tour> tourList = new ArrayList<Tour>();
				// if (pos == 0) {
				// tourList = fulllist;
				// } else {
				// for (int i = 0; i < fulllist.size(); i++) {
				// Tour t = fulllist.get(i);
				// List<String> c = t.getCategories();
				// if (c.contains(sel)) {
				// tourList.add(t);
				// }
				// }
				// }
				//
				// filterlist = tourList;
				//
				// // adapter.clear();
				// // adapter.addAll(tourList);
				// // adapter.notifyDataSetChanged();
				//
				// if (mSearchView != null) {
				// String s = mSearchView.getQuery().toString();
				//
				// if (s != null) {
				// search(s);
				// }
				// }

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

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

								// adapter.notifyDataSetChanged();
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
		// // e.printStackTrace();
		// // }
		// // adapter.notifyDataSetChanged();
		// // }
		// //
		// // });
	}

	public void search(String s) {
		mSearchView.setQuery(s, true);
	}

	public void select(Tour tour) {
		Intent start = new Intent(this, TourInfo.class);
		String title = tour.title;
		String description = tour.description;
		String imageUrl = tour.getImageUrl();
		Bundle b1 = new Bundle();
		b1.putString("tourTitle", title);
		b1.putString("tourDescription", description);
		b1.putString("imageUrl", imageUrl);
		start.putExtras(b1);
		startActivity(start);
	}

	public void categorize(String sel, int pos) {
		List<Tour> tourList = new ArrayList<Tour>();
		if (pos == 0) {
			tourList = fulllist;
		} else {
			for (int i = 0; i < fulllist.size(); i++) {
				Tour t = fulllist.get(i);
				List<String> c = t.getCategories();
				if (c.contains(sel)) {
					tourList.add(t);
				}
			}
		}

		filterlist = tourList;

		adapter.clear();
		adapter.addAll(tourList);
		adapter.notifyDataSetChanged();

		if (mSearchView != null) {
			String s = mSearchView.getQuery().toString();

			if (s != null) {
				search(s);
			}
		}
	}

	public void sort(View view) {

		List<Tour> tourList = new ArrayList<Tour>();
		if (view.getId() == R.id.viewed) {
			while (!fulllist.isEmpty()) {
				int v = 0;
				Tour t = null;
				for (int i = 0; i < fulllist.size(); i++) {
					Tour temp = fulllist.get(i);
					int vtemp = temp.getViews();
					if (vtemp > v) {
						t = temp;
						v = vtemp;
					}
				}
				tourList.add(t);
				fulllist.remove(t);
			}
			// tourList = server.getSortedTour("views");
		} else if (view.getId() == R.id.rated) {
			while (!fulllist.isEmpty()) {
				double r = 0;
				Tour t = null;
				for (int i = 0; i < fulllist.size(); i++) {
					Tour temp = fulllist.get(i);
					double rtemp = temp.getRating();
					if (rtemp > r) {
						t = temp;
						r = rtemp;
					}
				}
				tourList.add(t);
				fulllist.remove(t);
			}
			// tourList = server.getSortedTour("rating");
		} else if (view.getId() == R.id.alpha) {

			// tourList = server.getSortedTour("title");
		}

		fulllist = tourList;

		adapter.clear();
		adapter.addAll(tourList);
		adapter.notifyDataSetChanged();

		categorize(categories.getSelectedItem().toString(),
				categories.getSelectedItemPosition());
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
		if (arg0.equals("")) {
			tourList = filterlist;
		} else {
			for (int i = 0; i < filterlist.size(); i++) {
				Tour t = filterlist.get(i);
				if (t.getTitle().toLowerCase().contains(arg0.toLowerCase())
						|| t.getDescription().toLowerCase()
								.contains(arg0.toLowerCase())) {
					tourList.add(t);
				}
			}
		}
		adapter.clear();
		adapter.addAll(tourList);
		adapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		List<Tour> tourList = new ArrayList<Tour>();
		for (int i = 0; i < filterlist.size(); i++) {
			Tour t = filterlist.get(i);
			if (t.getTitle().toLowerCase().contains(arg0.toLowerCase())
					|| t.getDescription().toLowerCase()
							.contains(arg0.toLowerCase())) {
				tourList.add(t);
			}
		}

		adapter.clear();
		adapter.addAll(tourList);
		adapter.notifyDataSetChanged();
		return true;
	}

	protected boolean isAlwaysExpanded() {
		return true;
	}
}