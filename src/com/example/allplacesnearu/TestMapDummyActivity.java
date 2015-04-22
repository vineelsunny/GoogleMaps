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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
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


public class TestMapDummyActivity extends Activity implements OnItemClickListener  {
	AutoCompleteTextView autoc12;
	 Button findbutton12;
	 
	 String srcLocation,destLocation;
	 double destlat,destlng;
	 List<Address> destList;
	 
	 Address destAddr;
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
   			
   			Toast.makeText(this,"Testdestlat"+destlat+"Testdestlng"+destlng, Toast.LENGTH_LONG).show();
   			
   			Intent intent=new Intent();
   			intent.putExtra("destlat",destlat);
   			intent.putExtra("destlng",destlng);
   	        intent.putExtra("source",srcLocation);
   	        intent.putExtra("destination",destLocation);
   	        setResult(2,intent);
   	       
   	        finish();
   			
   			
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

