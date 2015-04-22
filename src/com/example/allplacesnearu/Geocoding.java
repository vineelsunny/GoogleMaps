package com.example.allplacesnearu;

import android.support.v4.app.FragmentActivity;

import java.io.IOException;
import java.util.List;
 

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

public class Geocoding extends FragmentActivity implements OnItemClickListener,OnMapClickListener{
	 GoogleMap googleMap;
	 AutoCompleteTextView autoc;
	 String location;
	 String locality;
	 List<Address> list;
	 Address addr;
	 //required for places api
	 private static final String LOG_TAG = "ExampleApp";
	 private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	 private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	 private static final String OUT_JSON = "/json";
     private static final String API_KEY ="AIzaSyD1qAgiN8O2D2RfR_6n45jJEuXESIz3eT4";//browser-key
     //till here
     
	public void onCreate(Bundle savedInstanceState)
	{
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity4);
	        autoc=(AutoCompleteTextView)findViewById(R.id.autocomplete);
	        autoc.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
		    autoc.setOnItemClickListener(this);
	        
	        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1);
	   	 
	        // Getting a reference to the map
	        googleMap = supportMapFragment.getMap();
	        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}
	
	public void gotoLocation(double lat,double lng,float zoom,String locality)
	{
		LatLng latlng=new LatLng(lat,lng);
		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latlng,zoom);	
		 Marker hyd = googleMap.addMarker(new MarkerOptions().position(latlng).title(locality));
		 System.out.println("@@"+hyd.getPosition());
        googleMap.animateCamera(update);
		
	}
	 @Override
	    public void onMapClick(LatLng arg0) {
	        // TODO Auto-generated method stub
	        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
	    }
	
	public void geoLocate(View v) throws IOException
	{
		hideSoftKeyboard(v);
		
		    Geocoder gc=new Geocoder(this);
		    location=autoc.getText().toString();
			list=gc.getFromLocationName(location,1);
			addr=list.get(0); 
			locality=addr.getLocality();
			Toast.makeText(this,locality,Toast.LENGTH_LONG).show();
			double lat=addr.getLatitude();
			double lng=addr.getLongitude();
			gotoLocation(lat,lng,15,location);
	
	}
	
	private void hideSoftKeyboard(View v) {
		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(),0);
	}
	
	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false&key=" + API_KEY);
	       // sb.append("&components=country:uk");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
	
	public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    private ArrayList<String> resultList;

	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }

	    @Override
	    public int getCount() {
	        return resultList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultList.get(index);
	    }

	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = autocomplete(constraint.toString());

	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	        return filter;
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String str = (String) arg0.getItemAtPosition(arg2);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		
	}
	public void onTiltMore(View view) {
        CameraPosition currentCameraPosition = googleMap.getCameraPosition();
        float currentTilt = currentCameraPosition.tilt;
        float newTilt = currentTilt + 20;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Called when the tilt less button (the one with the \) is clicked.
     */
    public void onTiltLess(View view) {
        CameraPosition currentCameraPosition = googleMap.getCameraPosition();

        float currentTilt = currentCameraPosition.tilt;

        float newTilt = currentTilt - 10;
        newTilt = (newTilt > 0) ? newTilt : 0;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, CancelableCallback callback) {
       
                googleMap.animateCamera(update, callback);      
    }

}
