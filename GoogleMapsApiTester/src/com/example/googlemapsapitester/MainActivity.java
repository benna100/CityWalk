package com.example.googlemapsapitester;


import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;


public class MainActivity extends MapActivity {
/** Called when the activity is first created. */

static TextView LocationText;
MapView mapView;


@Override
protected boolean isRouteDisplayed() {
        return false;
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  //Identifies the textview 'locationtext' as the variable LocationText. Planned usage just for debugging
    //LocationText = (TextView)findViewById(R.id.locationtext);

  //defines the mapview as variable 'mapView' and enables zoom controls
    mapView = (MapView) findViewById(R.id.mapview);
    mapView.setBuiltInZoomControls(true);

    /**
     * Code required to receive gps location. Activates GPS provider, and is set to update only after
     * at least 10 seconds and a position change of at least 10 metres
     */
    LocationListener locationListener = new MyLocationListener();

    //setting up the location manager
    LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, (float) 0.01, locationListener); 

    //Adds a current location overlay to the map 'mapView' and turns on the map's compass

    MyLocation myLocationOverlay = new MyLocation(this, mapView);
    myLocationOverlay.enableMyLocation();
    myLocationOverlay.enableCompass();

    mapView.getOverlays().add(myLocationOverlay);
    mapView.postInvalidate();
}
}