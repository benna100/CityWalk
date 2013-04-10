package com.example.googlemapsapitester;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener {

private boolean hasbearing;

/**
 * Code to run when the listener receives a new location
 */



@Override
public void onLocationChanged(Location locFromGps) {



    //Toast.makeText(getApplicationContext(), "Location changed, Lat: "+locFromGps.getLatitude()+" Long: "+ locFromGps.getLongitude(), Toast.LENGTH_SHORT).show();

    //LocationText.setText("Your Location: Latitude " +locFromGps.getLatitude() + " Longitude: " +locFromGps.getLongitude());

    double dbllatitude = locFromGps.getLatitude();
    double dbllongitude = locFromGps.getLongitude();
    double dblaltitude = locFromGps.getAltitude();
    float bearing = locFromGps.getBearing(); 

    //KestrelActivity.LocationText.setText("Your Location: Latitude " + dbllatitude + " Longitude: " +dbllongitude + " Altitude " + dblaltitude + " Bearing: " + bearing);



    hasbearing = locFromGps.hasBearing();


    if (hasbearing =  false) {

        //Toast.makeText(getApplicationContext(), "No bearing", Toast.LENGTH_SHORT).show();

    }
    else
    {
        //Toast.makeText(getApplicationContext(), "I HAZ bearing: " + locFromGps.getBearing(), Toast.LENGTH_SHORT).show();
    }


}

@Override
public void onProviderDisabled(String provider) {
   // called when the GPS provider is turned off (user turning off the GPS on the phone)
}

@Override
public void onProviderEnabled(String provider) {
   // called when the GPS provider is turned on (user turning on the GPS on the phone)
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
   // called when the status of the GPS provider changes

}
}
