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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DirectionIntro extends Activity implements OnItemClickListener{
	 AutoCompleteTextView autoc1;
	 AutoCompleteTextView autoc2;
	 Button findbutton;
	 //sending the lats and longs to DirectionActivity
	 String srcLocation,destLocation;
	 List<Address> srcList,destList;
	 Address srcAddr,destAddr;
	 public double srclat,srclng,destlat,destlng;
	 //till here
	 
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
	        setContentView(R.layout.directions_intro);
	        findbutton=(Button)findViewById(R.id.buttonfind);
	        autoc1=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView12);
	        autoc2=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView22);
	        autoc1.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
		    autoc1.setOnItemClickListener(this);
		    autoc2.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
		    autoc2.setOnItemClickListener(this);
		    
		    
	 }
	 public void geoLocate(View v) throws IOException
		{
			hideSoftKeyboard(v);
			
			    Geocoder gc=new Geocoder(this);
			    //getting the text
			    srcLocation=autoc1.getText().toString();
			    destLocation=autoc2.getText().toString();
			    //adding them to list
				srcList=gc.getFromLocationName(srcLocation,1);
				destList=gc.getFromLocationName(destLocation,1);
				//getting the 1st address object
				srcAddr=srcList.get(0);
				destAddr=destList.get(0);
				//finding the latitude & logitude of src and dest
				 srclat=srcAddr.getLatitude();
				srclng=srcAddr.getLongitude();
				destlat=destAddr.getLatitude();
				destlng=destAddr.getLongitude();
				Toast.makeText(this,"DIsrclat "+srclat+"DIsrclng "+srclng+"DIdestlat"+destlat+"DIdestlng"+destlng, Toast.LENGTH_LONG).show();
				
				Intent intent=new Intent();
				intent.putExtra("srclat",srclat);
				intent.putExtra("srclng",srclng);
				intent.putExtra("destlat",destlat);
				intent.putExtra("destlng",destlng);
		        intent.putExtra("source",srcLocation);
		        intent.putExtra("destination",destLocation);
		        setResult(2,intent);
		       
		        finish();
				
				//goToDirectionsActivity(srclat,srclng,destlat,destlng);
				//finish();
		}

		private void hideSoftKeyboard(View v) {
			InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(),0);
		}
		
		/*public void goToDirectionsActivity(double srclat,double srclng,double destlat,double destlng){
			Intent intent=new Intent(this,DirectionActivity.class);
			intent.putExtra("srclat",srclat);
			intent.putExtra("srclng",srclng);
			intent.putExtra("destlat",destlat);
			intent.putExtra("destlng",destlng);
			startActivity(intent);					
		}*/
		
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

}
