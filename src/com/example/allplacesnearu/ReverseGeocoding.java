package com.example.allplacesnearu;

import java.io.IOException;
import java.util.List;
 

import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
 
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReverseGeocoding extends FragmentActivity{
	
	    GoogleMap googleMap;
	    MarkerOptions markerOptions;
	    LatLng latLng;
	    TextView tv;
	    List<Address>  addresses ;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity3);
	        tv=(TextView)findViewById(R.id.location);
	        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
	 
	        // Getting a reference to the map
	        googleMap = supportMapFragment.getMap();
	 
	        // Setting a click event handler for the map
	        googleMap.setOnMapClickListener(new OnMapClickListener() {
	 
	            @Override
	            public void onMapClick(LatLng arg0) {
	            	
	            	 // Clears the previously touched position
	                googleMap.clear();
	 
	                // Getting the Latitude and Longitude of the touched location
	                latLng = arg0;
	              
	                
	               tv.setText("lt"+latLng.latitude+" ln:"+latLng.longitude);
	 
	                // Animating to the touched position
	                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	                
	                // Creating a marker
	                markerOptions = new MarkerOptions();
	 
	                // Setting the position for the marker
	                markerOptions.position(latLng);
	 
	                
	 
	                // Adding Marker on the touched location with address
	                new ReverseGeocodingTask(getBaseContext()).execute(latLng);
	 
	            }
	        });
	    }
	 
	    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>{
	        Context mContext;
	 
	        public ReverseGeocodingTask(Context context){
	            super();
	            mContext = context;
	        }
	 
	        // Finding address using reverse geocoding
	        @Override
	        protected String doInBackground(LatLng... params) {
	            Geocoder geocoder = new Geocoder(mContext,Locale.ENGLISH);
	            double latitude = params[0].latitude;
	            double longitude = params[0].longitude;
	 
	            String addressText="";
	 
	            try {
	            	addresses = geocoder.getFromLocation(latitude, longitude,1);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	 
	            if(addresses != null && addresses.size() > 0 ){
	                Address address = addresses.get(0);
	 
	                addressText = String.format("%s, %s, %s",
	                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                address.getLocality(),
	                address.getCountryName());
	            }
	 
	            return addressText;
	        }
	 
	        @Override
	        protected void onPostExecute(String addressText) {
	            // Setting the title for the marker.
	            // This will be displayed on taping the marker
	        	markerOptions.title(addressText);
	        	// Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
	            
	
	 
	        }
	    }
	 

}
