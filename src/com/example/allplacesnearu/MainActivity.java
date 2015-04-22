package com.example.allplacesnearu;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapLongClickListener,OnMapClickListener,OnMarkerDragListener, android.location.LocationListener {
	GoogleMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
         
         int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
         
         if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        	 
             int requestCode = 10;
             Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
             dialog.show();
  
         }else{
        	 SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map134);
        	 map = fm.getMap();
        	 
        	 map.setMyLocationEnabled(true);
        	 map.setOnMarkerDragListener(this);
             map.setOnMapLongClickListener(this);
             map.setOnMapClickListener(this);
             map.setMyLocationEnabled(true);
        	 
     
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
     }
 }
			  /*
			   
			   LatLng mapCenter = new LatLng(17.12889,78.34622);
		       map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));
		       CameraPosition cameraPosition = CameraPosition.builder()
		                .target(mapCenter)
		                .zoom(13)
		                .bearing(90)
		                .build();
		    // Animate the change in camera view over 2 seconds
		        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
		                2000, null);

		        // Flat markers will rotate when the map is rotated,
		        // and change perspective when the map is tilted.
		        map.addMarker(new MarkerOptions()
		                .position(mapCenter).title("17.12889,78.34622")
		                .flat(true)).showInfoWindow();		   */     
		        

	 @Override
	    public void onMarkerDrag(Marker arg0) {
	        // TODO Auto-generated method stub
	         
	    }
	 
	    @Override
	    public void onMarkerDragEnd(Marker arg0) {
	        // TODO Auto-generated method stub
	        LatLng dragPosition = arg0.getPosition();
	        double dragLat = dragPosition.latitude;
	        double dragLong = dragPosition.longitude;
	        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
	        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
	    }
	 
	    @Override
	    public void onMarkerDragStart(Marker arg0) {
	        // TODO Auto-generated method stub
	         
	    }
	 
	    @Override
	    public void onMapClick(LatLng arg0) {
	        // TODO Auto-generated method stub
	        map.animateCamera(CameraUpdateFactory.newLatLng(arg0));
	    }
	 
	 
	    @Override
	    public void onMapLongClick(LatLng arg0) {
	        // TODO Auto-generated method stub
	     
	        //create new marker when user long clicks
	        map.addMarker(new MarkerOptions()
	        .position(arg0)
	        .draggable(true));
	    }   
	    @Override
	    public void onLocationChanged(Location location) {
	 
	 
	        // Getting latitude of the current location
	        double latitude = location.getLatitude();
	 
	        // Getting longitude of the current location
	        double longitude = location.getLongitude();
	 
	        // Creating a LatLng object for the current location
	        LatLng latLng = new LatLng(latitude, longitude);
	        map.addMarker(new MarkerOptions()
            .position(latLng).title("You R here")
            .flat(true)).showInfoWindow();
	 
	        // Showing the current location in Google Map
	        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	 
	        // Zoom in the Google Map
	        map.animateCamera(CameraUpdateFactory.zoomTo(15));
	 
	 
	    }
	 public void onProviderDisabled(String provider) {
	        // TODO Auto-generated method stub
	    }
	 
	    public void onProviderEnabled(String provider) {
	        // TODO Auto-generated method stub
	    }
	 
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	        // TODO Auto-generated method stub
	    }
}
