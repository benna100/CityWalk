package com.example.citywalkapplayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

//import com.example.googlemapsapitester.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.MapView;

import android.support.v4.app.FragmentActivity;

public class GoogleMapActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, LocationListener {

	private GoogleMap mMap;
	private LocationListener mLocationListener;
	private static final OnMarkerClickListener OnMarkerClickListener = null;
	private List<LatLng> tour1 = null;
	private UiSettings mUiSettings;
	private Tour tour;
	private List<Notes> notesList;
	private List<Notes> notesOrder;
	protected LocationManager locationManager;
	//private LocationListener locationListener;
	private static int noteNumber = 0;
	private Vibrator vibrator;
	private boolean[] hasFired;

	MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			Bundle b = getIntent().getExtras();
			noteNumber = b.getInt("noteNumber");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.activity_googlemapactivity);
		
		
		addListeners();
		setupMap();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// TextView textView = (TextView) findViewById(R.id.locationText);
		// float [] dist = new float[1];
		// Location.distanceBetween(dtu.latitude, dtu.longitude, dtu2.latitude,
		// dtu2.longitude, dist);
		// textView.setText(Float.toString(dist[0]));
	}

	protected void calculateDistanceToNextPoint(Location currentLocation) {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.locationText);
		Notes note = tour.notesList.get(noteNumber);
		LatLng locationOfNextNode = convertLocationToLatLng(note.location);

		// float dist = location.distanceTo(locationKBH);
		float[] dist = new float[1];
		Location.distanceBetween(locationOfNextNode.latitude,
				locationOfNextNode.longitude, currentLocation.getLatitude(),
				currentLocation.getLongitude(), dist);
		textView.setText(Float.toString(dist[0]));
		setCamera(new LatLng(currentLocation.getLatitude(),
				currentLocation.getLongitude()));
		if (dist[0] < 10.0) {
			// Notes note = (Notes) tour.notesList.get(noteNumber);
			// Intent intent = new Intent(this, NoteInfo.class);
			// Bundle b1 = new Bundle();
			// String description = note.description;
			// b1.putString("noteDescription", description);
			// intent.putExtras(b1);
			// intent.putExtra("number", noteNumber);
			// startActivity(intent);

			if (!hasFired[noteNumber]) {
				hasFired[noteNumber] = true;
				vibrator.vibrate(300);
			}
			boolean finish = false;
			if (noteNumber == notesList.size()-1) finish = true;
			displayNoteInfo(noteNumber, finish);

		}

	}

	public void incrementNoteNumber() {
		if (!(noteNumber == notesList.size()-1)) {
			noteNumber++;
		}
		
		
	}

	public LatLng convertLocationToLatLng(String location) {

		LatLng newLocation = new LatLng(
				Double.parseDouble(location.split(";")[0]),
				Double.parseDouble(location.split(";")[1]));
		return newLocation;

	}

	private void setupMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map))

			.getMap();
		}

		tour = StartActivity.selected;
		// Tour tour = new Tour();
		// ServerAccessLayer server = new ServerAccessLayer();
		// tour = server.getTour("2");
		// server.getSortedTour("rating");
		// setCamera(copenhagen1);
		notesList = tour.getNoteList();
		notesOrder = new ArrayList<Notes>();
		for (String index : tour.getNoteOrder()){
			int i = Integer.parseInt(index);
			for (Notes note : notesList){
				if (note.getId()==i){
					notesOrder.add(note);
				}
			}
		}
		
		setTitleOfNextStop();
		
		mUiSettings = mMap.getUiSettings();

		// Get the location points for a specific tour
		List<LatLng> tourLocations = getTourLocations();

		// Draw the location points connected to a tour as a overlay
		drawOverlay(tourLocations);

		// Draw markers for all the notes
		//List<Notes> notesList = tour.getNoteList();

		for (int i = 0; i < notesList.size(); i++) {
			String noteType = notesList.get(i).getClass().getName();
			Notes note = notesList.get(i);
			drawMarker(noteType, note);
			System.out.println(noteType);

		}
		hasFired = new boolean[notesList.size()];
		for (int i = 0; i < hasFired.length; i++) {
			hasFired[i] = true;
		}
		tourLocations.listIterator();

		enableBasicMapFunctionality();

	}
	private void setTitleOfNextStop() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.nextStop);
		textView.setText("Next stop: " + notesOrder.get(noteNumber).getTitle());
	}

	public Notes getCurrentNode(){
		return notesOrder.get(noteNumber);
	}
	private void addListeners(){
		// Location
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
		
		// Button listeners
		Button nextNote = (Button)findViewById(R.id.skipStop);
		nextNote.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	skipNote();
		    }
		});
		
		Button backToMenu = (Button)findViewById(R.id.backToMenu);
		backToMenu.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	backToMenu();
		    }
		});
	}

	protected void backToMenu() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, StartActivity.class);
    	startActivity(intent);
	}

	protected void skipNote() {
		if (noteNumber == notesList.size()-1) finishActivity();
		// TODO Auto-generated method stub
		incrementNoteNumber();
		
		setTitleOfNextStop();
	}

	private void finishActivity() {
		// TODO Auto-generated method stub
		Intent start = new Intent(this, FinishActivity.class);
		startActivity(start);
	}

	public void enableBasicMapFunctionality() {
		mMap.setOnInfoWindowClickListener(this);

		setCamera(convertLocationToLatLng(getCurrentNode().location));
		TextView textView = (TextView) findViewById(R.id.locationText);
		textView.setText(getCurrentNode().noteTitle);
		// mMap.addPolyline((new
		// PolylineOptions()).add(copenhagen1,copenhagen2));

		mMap.setMyLocationEnabled(true);
		mUiSettings.setCompassEnabled(true);

		mUiSettings.setRotateGesturesEnabled(true);

		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location location) {
				nextNoteEvent(location);

			}
		});
	}

	public void drawOverlay(List<LatLng> tourLocations) {
		List<LatLng> geoPoints = new ArrayList<LatLng>();
		int geoPointCounter = 0;
		for (LatLng geoPoint : tourLocations) {
			geoPoints.add(geoPoint);
			if ((geoPointCounter != 0)
					&& (geoPointCounter != tourLocations.size())) {
				mMap.addPolyline((new PolylineOptions())
						.add(geoPoints.get(geoPointCounter - 1),
								geoPoints.get(geoPointCounter)).width(6)
						.color(Color.BLUE));
			}
			geoPointCounter += 1;
		}
	}

	public ArrayList<LatLng> getTourLocations() {
		ArrayList<LatLng> tour1 = new ArrayList<LatLng>();
		for (int i = 0; i < tour.getTourLocations().size(); i++) {
			for (int j = 0; j < tour.getTourLocations().size(); j++) {
				tourLocations location = new tourLocations();
				location = tour.getTourLocations().get(i);
				// String indexNumber =
				// Integer.toString(location.locationIndex);
				if (location.locationIndex == j) {
					String latLngLocation = location.location;
					String[] latLng = latLngLocation.split(";");
					double lat = Double.parseDouble(latLng[0]);
					double lng = Double.parseDouble(latLng[1]);
					LatLng latLngPosition = new LatLng(lat, lng);
					tour1.add(latLngPosition);
				}
			}
		}
		return tour1;
	}

	public void setCamera(LatLng latlng) {
		if (mMap != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18.0f));
		}
	}

	public void drawLine() {

	}

	public void drawMarker(String noteType, Notes note) {
		if (noteType.equals("com.example.citywalkapplayout.POI")) {
			POI poiNote = new POI();
			// poiNote = (POI) notesList.get(i);
			poiNote = (POI) note;
			System.out.println("as");
			String title = poiNote.noteTitle;
			String location = poiNote.location;
			String[] latLng = location.split(";");
			double lat = Double.parseDouble(latLng[0]);
			double lng = Double.parseDouble(latLng[1]);
			LatLng notePosition = new LatLng(lat, lng);
			Marker mark = mMap.addMarker(new MarkerOptions().position(
					notePosition).title(title));
		} else if (noteType.equals("com.example.citywalkapplayout.TourNotes")) {
			TourNotes tourNote = new TourNotes();
			// tourNote = (TourNotes) notesList.get(i);
			tourNote = (TourNotes) note;
			String location = tourNote.location;
			String title = tourNote.noteTitle;
			String[] latLng = location.split(";");
			double lat = Double.parseDouble(latLng[0]);
			double lng = Double.parseDouble(latLng[1]);
			LatLng notePosition = new LatLng(lat, lng);
			Marker mark = mMap.addMarker(new MarkerOptions().position(
					notePosition).title(title));

		}
	}

	private JSONObject getDirectionObjectFromMaps(Tour tour) {
		try {
			URL url = new URL(
					"http://maps.googleapis.com/maps/api/directions/json?origin=55.691084,%2012.560892&destination=55.699647,%2012.577114&sensor=false");
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String directions = "";
			String line = null;

			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				directions = directions + line;
			}
			reader.close();
			JSONObject json = new JSONObject(directions);
			System.out.println("adad");
			return json;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String[] route(JSONObject mapsDirections) {
		try {
			// start = directions.getJSONArray("start_location");

			String routes = mapsDirections.getString("routes");
			// System.out.println(routes);

			String legs = new JSONObject(routes.substring(1,
					routes.length() - 1)).getString("legs");
			JSONObject start = new JSONObject(new JSONObject(legs.substring(1,
					legs.length() - 1)).getString("start_location"));
			String startLng = start.getString("lng");
			String startLat = start.getString("lat");
			// JSONObject start2 =
			// SONArray legs = directions.getJSONArray("routes");
			JSONArray steps = new JSONObject(legs.substring(1,
					legs.length() - 1)).getJSONArray("steps");
			// JSONArray jsonSteps = new JSONArray(steps.substring(1,
			// steps.length()-1));
			String[] waypoints = new String[steps.length()];
			for (int i = 0; i < steps.length(); i++) {
				JSONObject waypoint = steps.getJSONObject(i);
				JSONObject oStart = waypoint.getJSONObject("start_location");
				JSONObject oEnd = waypoint.getJSONObject("end_location");
				String wStart = oStart.getString("lat") + ","
						+ oStart.getString("lng");
				String wEnd = oEnd.getString("lat") + ","
						+ oEnd.getString("lng");

				// JSONArray jStart = waypoint.getJSONArray("start_location");
				// JSONArray jEnd = waypoint.getJSONArray("end_location");
				waypoints[i] = wStart + wEnd;
			}
			System.out.println("hello");
			// jsonSteps.getJSONArray(");
			// String steps = routes.getString("legs")1
			return waypoints;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean onMarkerClick(final Marker marker) {

		return true;
	}

	public void nextNoteEvent(Location location) {

		// if(location)
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.googlemapactivity, menu);
		return true;
	}

	public void displayNoteInfo(int noteNumber, boolean finish) {
		Notes note = notesList.get(noteNumber);
		Intent start = new Intent(this, NoteInfo.class);

		Bundle b1 = new Bundle();
		b1.putString("noteTitle", note.noteTitle);
		b1.putString("noteDescription", note.description);
		b1.putString("imageUrl", note.imageUrl);
		b1.putBoolean("finish", finish);
		start.putExtras(b1);
		start.putExtra("number", noteNumber);
		startActivity(start);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		
		for (int i = 0; i < notesList.size(); i++) {
			Notes poiNote = (Notes) notesList.get(i);
			String title = poiNote.noteTitle;
			if (marker.getTitle().equals(title)) {
				displayNoteInfo(i, false);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		calculateDistanceToNextPoint(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
