package com.example.citywalkapplayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

public class StartActivity extends Activity implements
		SearchView.OnQueryTextListener {
	private ProgressDialog progressDialog;
	public static Tour selected;
	float x = 0;
	float y = 0;
	int pos = 0;
	View lastSort;

	static List<Tour> fulllist = new ArrayList<Tour>();
	static boolean firstTime = true;

	static List<Tour> viewlist = new ArrayList<Tour>();
	static List<Tour> ratelist = new ArrayList<Tour>();
	static List<Tour> datelist = new ArrayList<Tour>();
	static List<Tour> proxlist = new ArrayList<Tour>();

	List<Tour> tours = new ArrayList<Tour>();
	List<Tour> filterlist = new ArrayList<Tour>();
	ServerAccessLayer server = new ServerAccessLayer();
	CityWalkTourAdapter adapter;

	private SearchView mSearchView;
	Spinner categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_start);
		if (firstTime) {
			firstTime = false;
			setContentView(R.layout.splash);
			new LoadViewTask().execute();
		} else {
			setContentView(R.layout.activity_start);
			setListenersAndAdapters();
		}

	}

	public void setListenersAndAdapters() {
		lastSort = findViewById(R.id.viewed);
		lastSort.setBackgroundResource(R.drawable.blue);

		tours.addAll(fulllist);

		filterlist.addAll(fulllist);
		;

		// });

		ListView listView = (ListView) findViewById(R.id.list);
		categories = (Spinner) findViewById(R.id.categories);
		categories.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String sel = parent.getSelectedItem().toString();
				categorize(sel, pos);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		adapter = new CityWalkTourAdapter(this, tours);

		listView.setAdapter(adapter);

		// listView.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// ListView layout = (ListView) v;
		// int x = (int) event.getX();
		// int y = (int) event.getY();
		// int pos;
		// int un = 0;
		// boolean uneven = false;
		//
		// for (int i = 0; i < layout.getChildCount(); i++) {
		// View view = layout.getChildAt(i);
		//
		// Rect outRect = new Rect(view.getLeft(), view.getTop(),
		// view.getRight(), view.getBottom());
		//
		// if (outRect.contains(x, y)) {
		// pos = (i + 1) * 2 - (i + 1);
		// if (tours.size() + 1 > pos) {
		// if (tours.size() % 2 == 1) {
		// un = tours.size() - 1;
		// uneven = true;
		// }
		//
		// int w = view.getWidth();
		// if (x < w / 2 | pos > un & uneven) {
		// // view.setBackgroundResource(R.drawable.blue);
		// pos -= 1;
		//
		// // TextView views = (TextView) view.findViewById(R.id.title1);
		// // views.setText("SELECTED!!!");
		// // views.setBackgroundResource(R.drawable.blue);
		//
		// GridLayout views = (GridLayout) view.findViewById(R.id.grid1);
		// views.setBackgroundResource(R.drawable.blue);
		// }else{
		// GridLayout views = (GridLayout) view.findViewById(R.id.grid2);
		// views.setBackgroundResource(R.drawable.blue);
		// }
		//
		// selected = tours.get(pos);
		// // selected.setTitle("SELECTED!!!");
		// //
		// // adapter.notifyDataSetChanged();
		//
		// select(selected);
		//
		// }
		// }
		// }
		//
		// }
		// return true;
		// }
		// });
	}

	public void search(String s) {
		mSearchView.setQuery(s, true);
	}

	public void select(View view) {
		TextView tid;
		if (view.getId() == R.id.grid1) {
			tid = (TextView) view.findViewById(R.id.tourid1);
		} else {
			tid = (TextView) view.findViewById(R.id.tourid2);
		}

		Tour tour = new Tour();

		for (int i = 0; i < fulllist.size(); i++) {
			Tour t = fulllist.get(i);
			String s = t.getId() + "";
			if (s.equals(tid.getText())) {
				tour = t;
				break;
			}
		}
		
		selected = tour;

//		TextView te = (TextView) view.findViewById(R.id.title1);

		view.setBackgroundResource(R.drawable.blue);

		Intent start = new Intent(this, TourInfo.class);
		String title = tour.title;
		String description = tour.description;
		int id = tour.getId();
		Bundle b1 = new Bundle();
		b1.putString("tourTitle", title);
		b1.putString("tourDescription", description);
		b1.putInt("id", id);
		start.putExtras(b1);
		startActivity(start);
	}

	public void categorize(String sel, int pos) {
		List<Tour> tourList = new ArrayList<Tour>();
		if (pos == 0) {
			tourList.addAll(fulllist);
		} else {
			for (int i = 0; i < fulllist.size(); i++) {
				Tour t = fulllist.get(i);
				List<String> c = t.getCategories();
				if (c.contains(sel)) {
					tourList.add(t);
				}
			}
		}

		filterlist.clear();
		filterlist.addAll(tourList);

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

	public void sort() {
		List<Tour> tourList = new ArrayList<Tour>();

		// initiate
		viewlist.addAll(fulllist);
		ratelist.addAll(fulllist);
		datelist.addAll(fulllist);
		proxlist.addAll(fulllist);

		// View sort
		while (!viewlist.isEmpty()) {
			int v = 0;
			Tour t = null;
			for (int i = 0; i < viewlist.size(); i++) {
				Tour temp = viewlist.get(i);
				if (temp.equals(null)) {
					continue;
				}
				int vtemp = temp.getViews();
				if (vtemp > v) {
					t = temp;
					v = vtemp;
				}
			}

			if (t != null) {
				tourList.add(t);
			}
			viewlist.remove(t);
		}
		viewlist.addAll(tourList);

		// Rate sort
		tourList.clear();
		while (!ratelist.isEmpty()) {
			double r = 0;
			Tour t = null;
			for (int i = 0; i < ratelist.size(); i++) {
				Tour temp = ratelist.get(i);
				if (temp.equals(null)) {
					continue;
				}
				double rtemp = temp.getRating();
				if (rtemp > r) {
					t = temp;
					r = rtemp;
				}
			}
			if (t != null) {
				tourList.add(t);
			}
			ratelist.remove(t);
			// tourList = server.getSortedTour("rating");
		}
		ratelist = tourList;

		// Date sort

		// Proximity sort
		fulllist.clear();
		fulllist.addAll(viewlist);
	}

	public void sortSelect(View view) {
		lastSort.setBackgroundResource(R.drawable.dark_gradient);
		view.setBackgroundResource(R.drawable.blue);
		lastSort = view;

		if (view.getId() == R.id.viewed) {
			fulllist = viewlist;
		} else if (view.getId() == R.id.rated) {
			fulllist = ratelist;
		}

		adapter.clear();
		adapter.addAll(fulllist);
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
				if (t.getTitle().toLowerCase(Locale.ENGLISH).contains(arg0.toLowerCase(Locale.ENGLISH))
						|| t.getDescription().toLowerCase(Locale.ENGLISH)
								.contains(arg0.toLowerCase(Locale.ENGLISH))) {
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
			if (t.getTitle().toLowerCase(Locale.ENGLISH).contains(arg0.toLowerCase(Locale.ENGLISH))
					|| t.getDescription().toLowerCase(Locale.ENGLISH)
							.contains(arg0.toLowerCase(Locale.ENGLISH))) {
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

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// Before running code in separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(StartActivity.this);
			// Set the progress dialog to display a horizontal progress bar
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Set the dialog title to 'Loading...'
			progressDialog.setTitle("City Walk");
			// Set the dialog message to 'Loading application View, please
			// wait...'
			progressDialog
					.setMessage("Loading application, please wait...");
			// This dialog can't be canceled by pressing the back key
			progressDialog.setCancelable(false);
			// This dialog isn't indeterminate
			progressDialog.setIndeterminate(false);
			// The maximum number of items is 100
			progressDialog.setMax(100);
			// Set the current progress to zero
			progressDialog.setProgress(0);
			// Display the progress dialog
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			/*
			 * This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This is
			 * where the code that is going to be executed on a background
			 * thread must be placed.
			 */
			try {
				// Get the current thread's token
				synchronized (this) {
					//progressDialog.setMessage("Loading tours..");
					publishProgress(10);
					fulllist = server.getSortedTour("views");
					// Initialize an integer (that will act as a counter) to
					// zero

					// Set the current progress.
					// This value is going to be passed to the
					// onProgressUpdate() method.
//					progressDialog.setMessage("Loading pictures..");
					publishProgress(50);
					for(int i = 0;i < fulllist.size();i++){
						Tour t = fulllist.get(i);
						t.setImg((BitmapDrawable) drawableFromUrl(t.getImageUrl()));
					}

//					progressDialog.setMessage("Applying scripts..");
					publishProgress(70);
					sort();
					
					publishProgress(100);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			progressDialog.dismiss();
			// initialize the View
			setContentView(R.layout.activity_start);
			setListenersAndAdapters();
		}
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
}