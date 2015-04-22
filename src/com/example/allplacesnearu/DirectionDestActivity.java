package com.example.allplacesnearu;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DirectionDestActivity extends Activity implements OnItemClickListener, LocationListener {
	AutoCompleteTextView autoc12;
	 Button findbutton12;
	 double srclatitude,srclongitude;
	//sending the lats and longs to DirectionActivity
		 String srcLocation,destLocation;
		 List<Address> srcList,destList;
		 Address srcAddr,destAddr;
		 public double srclat,srclng,destlat,destlng;
		 //till here
	 DirectionDestActivityMain ddac;
	 //required for places api
	 private static final String LOG_TAG = "ExampleApp";
	 private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	 private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	 private static final String OUT_JSON = "/json";
   private static final String API_KEY ="AIzaSyD1qAgiN8O2D2RfR_6n45jJEuXESIz3eT4";//browser-key
   //till here
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.me_to_dest);
       findbutton12=(Button)findViewById(R.id.buttonfind12);
       autoc12=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView678);
       autoc12.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	    autoc12.setOnItemClickListener(this);
   }
   public void geoLocate(View v) throws IOException
	{
		hideSoftKeyboard(v);
		findMyLocation();
		
		    Geocoder gc=new Geocoder(this);
		    //getting the text
		    destLocation=autoc12.getText().toString();
		    //adding them to list
			srcLocation="You Are Here";
			destList=gc.getFromLocationName(destLocation,1);
			//getting the 1st address object
		
			destAddr=destList.get(0);
			//finding the latitude & logitude of src and dest
			 
			destlat=destAddr.getLatitude();
			destlng=destAddr.getLongitude();
			
			Toast.makeText(this,"DIdestlat"+destlat+"DIdestlng"+destlng, Toast.LENGTH_LONG).show();
			
			/*Intent intent=new Intent();
			intent.putExtra("srclat",srclatitude);
			intent.putExtra("srclng",srclongitude);
			intent.putExtra("destlat",destlat);
			intent.putExtra("destlng",destlng);
	        intent.putExtra("source",srcLocation);
	        intent.putExtra("destination",destLocation);
	        setResult(2,intent);
	       
	        finish();*/
			
			//goToDirectionsActivity(srclat,srclng,destlat,destlng);
			//finish();
	}
   private void findMyLocation() {
	   int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
	   
       // Showing status
       if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

           int requestCode = 10;
           Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
           dialog.show();

       }
       else { // Google Play Services are available
    	   

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
       }
	
}

   public void onLocationChanged(Location location) {

     
       // Getting latitude of the current location
       srclatitude = location.getLatitude();

       // Getting longitude of the current location
       srclongitude = location.getLongitude();
       
       Toast.makeText(this,"SRCLAT="+srclatitude+"SRCLNG="+srclongitude,Toast.LENGTH_LONG).show();
    

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
		
					String str = (String) arg0.getItemAtPosition(arg2);
			        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		
	}

}
