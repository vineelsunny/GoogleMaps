package com.example.allplacesnearu;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener;
import com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaClickListener;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class StreetViewActivity extends FragmentActivity implements OnStreetViewPanoramaClickListener, OnStreetViewPanoramaChangeListener, OnMarkerDragListener {
	
	 private static final LatLng SYDNEY = new LatLng(-33.87365,151.20689);
	 private GoogleMap mMap;
	    private Marker marker;
	 //private static final LatLng TAJ = new LatLng(27.1742,78.0419);
	 
	 //private static final LatLng Times = new LatLng(40.758558,-73.985178);
	 //private static final LatLng Char= new LatLng(17.361902,78.474728);
	//17.361679,78.474775
	 //17.361709,78.474776
	 //chow17.358204,78.47163
		//27.1742° N, 78.0419° E
	private StreetViewPanorama mSvp;
	@Override
	public void onCreate(Bundle savedInastanceState){
		super.onCreate(savedInastanceState);
		setContentView(R.layout.streetview);
	
		setUpStreetViewPanoramaIfNeeded(savedInastanceState);
		setUpMapIfNeeded();
		
	}

	private void setUpStreetViewPanoramaIfNeeded(Bundle savedInstanceState) {
	  if (mSvp == null) {
		  mSvp=((SupportStreetViewPanoramaFragment)
			        getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama))
			            .getStreetViewPanorama();
	    /*StreetViewPanoramaLocation location = mSvp.getLocation();
	    if (location != null && location.links != null) {
	        mSvp.setPosition(location.links[0].panoId);*/
	    }
	    if (mSvp != null) {
	      if (savedInstanceState == null) {
	        mSvp.setPosition(SYDNEY);
	        mSvp.setStreetNamesEnabled(true);
	        mSvp.setZoomGesturesEnabled(true);
	        mSvp.setUserNavigationEnabled(true);
	        mSvp.setPanningGesturesEnabled(true);
	      }
	      mSvp.setOnStreetViewPanoramaChangeListener(this);
          mSvp.setOnStreetViewPanoramaClickListener(this);
	    }
	 }
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map90))
                .getMap();
           
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
          		 mMap.animateCamera(CameraUpdateFactory.zoomTo(4));
            }
        }
    }
	private void setUpMap() {
        mMap.setOnMarkerDragListener(this);
         
       
        // Creates a draggable marker. Long press to drag.
        marker = mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .draggable(true));
     
    }
	 @Override
	    public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation orientation) {
	        Point point = mSvp.orientationToPoint(orientation);
	        if (point != null) {
	            mSvp.animateTo(new StreetViewPanoramaCamera.Builder().orientation(orientation)
	                .zoom(mSvp.getPanoramaCamera().zoom).build(), 1000);
	        }
	    }
	 @Override
	    public void onMarkerDragStart(Marker marker) {
	    }

	    @Override
	    public void onMarkerDragEnd(Marker marker) {
	        mSvp.setPosition(marker.getPosition(), 150);
	    }

	    @Override
	    public void onMarkerDrag(Marker marker) {
	    }

		@Override
		public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
			if (location != null) {
		          marker.setPosition(location.position);
		        }
			
		}
}

