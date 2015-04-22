package com.example.allplacesnearu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TestingPolylines extends FragmentActivity implements LocationListener {
	GoogleMap googleMap;
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.find_me);


	
	 // Getting Google Play availability status
    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

    // Showing status
    if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

        int requestCode = 10;
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
        dialog.show();

    }else { // Google Play Services are available

        // Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        addPolylines();
	}

}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPolylines()
	{
		PolylineOptions rectOptions = new PolylineOptions()
        .add(new LatLng(37.35, -122.0))
        .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
        .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
        .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
        .add(new LatLng(37.35, -122.0)); // Closes the polyline.

    // Get back the mutable Polyline
    Polyline polyline = googleMap.addPolyline(rectOptions);
	}
}
