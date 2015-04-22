package com.example.allplacesnearu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class TestMapActivity extends FragmentActivity implements LocationListener {
	 GoogleMap map;
	RadioButton rbDriving;
	RadioButton rbBiCycling;
   RadioButton rbWalking;
	    RadioGroup rgModes;
	    ArrayList<LatLng> markerPoints;
	    TextView tvDistanceDuration;
	    int mMode=0;
	    final int MODE_DRIVING=0;
	    final int MODE_BICYCLING=1;
	    final int MODE_WALKING=2;
	    double srclat,srclng,destlat,destlng;
	    Button findsrcdest;
	    LatLng srcLatLng,destLatLng;
	    String sourcelocation,destinationlocation;
	    double srclatitude,srclongitude;
	 
	//sending the lats and longs to DirectionActivity
		 String srcLocation,destLocation;
		 List<Address> srcList,destList;
		 Address srcAddr,destAddr;
		
		 //till here
	
	 
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.directions);
       findsrcdest=(Button)findViewById(R.id.findsrcdest);
       
       findMyLocation();
    // Getting reference to rb_driving
       rbDriving = (RadioButton) findViewById(R.id.rb_driving);

       // Getting reference to rb_bicylcing
       rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);

       // Getting reference to rb_walking
       rbWalking = (RadioButton) findViewById(R.id.rb_walking);

       // Getting Reference to rg_modes
       rgModes = (RadioGroup) findViewById(R.id.rg_modes);
       rgModes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
       	 
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {

               // Checks, whether start and end locations are captured
               if(markerPoints.size() >= 2){
                   LatLng origin = markerPoints.get(0);
                   LatLng dest = markerPoints.get(1);

                   // Getting URL to the Google Directions API
                   String url = getDirectionsUrl(origin, dest);

                   DownloadTask downloadTask = new DownloadTask();

                   // Start downloading json data from Google Directions API
                   downloadTask.execute(url);
               }
           }
       });


       tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);

       // Initializing
       markerPoints = new ArrayList<LatLng>();

       // Getting reference to SupportMapFragment of the activity_main
       SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map89);

       // Getting Map for the SupportMapFragment
       map = fm.getMap();

       // Enable MyLocation Button in the Map
       map.setMyLocationEnabled(true);
       
       
       
       findsrcdest.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			markerPoints.clear();		//so that the array will be clear wen v try to locate for the second time	
			Intent intent=new Intent(TestMapActivity.this,TestMapDummyActivity.class);
			startActivityForResult(intent,2);
			
		}
    	   
       });
       
       
   }
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
         super.onActivityResult(requestCode, resultCode, data);

          // check if the request code is same as what is passed  here it is 2
         if(requestCode==2)
            {
             if(null!=data)
             {         
                 // fetch the data
                 srclat=srclatitude;
                 srclng=srclongitude;
                 destlat=data.getDoubleExtra("destlat",-33.8130863);
                 destlng=data.getDoubleExtra("destlng",151.2887794);
                 
                 sourcelocation=data.getStringExtra("source");
                 destinationlocation=data.getStringExtra("destination");
                 
                 Toast.makeText(this,"DAsrclat "+srclat+"DAsrclng "+srclng+"DAdestlat"+destlat+"DAdestlng"+destlng, Toast.LENGTH_LONG).show();
                 
                 srcLatLng=new LatLng(srclat,srclng);
                 destLatLng=new LatLng(destlat,destlng);
                 markerPoints.add(srcLatLng);
                 markerPoints.add(destLatLng);
                 plotMarkersOnMap(markerPoints,sourcelocation,destinationlocation);
             }
            }
 }
   public void plotMarkersOnMap(ArrayList<LatLng> markerPoints2,String srcL,String destL) {
		map.clear();
		if(markerPoints2.size()==2)
		{
			LatLng origin=markerPoints2.get(0);
			LatLng dest=markerPoints2.get(1);
			CameraUpdate update=CameraUpdateFactory.newLatLngZoom(origin,11);	
			 MarkerOptions options1 = new MarkerOptions();
			 MarkerOptions options2 = new MarkerOptions();
			 options1.position(origin).title(srcL).snippet("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			 options2.position(dest).title(destL).snippet("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			 map.addMarker(options1);
			 map.addMarker(options2);
			map.animateCamera(update);
		
               // Getting URL to the Google Directions API
               String url = getDirectionsUrl(origin, dest);

               DownloadTask downloadTask = new DownloadTask();

               // Start downloading json data from Google Directions API
               downloadTask.execute(url);
         
			
		}
		
		
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
          
          if(destLatLng !=null){
        	  markerPoints=new ArrayList<LatLng>();
        	  markerPoints.add(new LatLng(srclatitude, srclongitude));
        	  markerPoints.add(destLatLng);
        	  plotMarkersOnMap(markerPoints,sourcelocation,destinationlocation);
          }
      
      

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
      
      
   private String getDirectionsUrl(LatLng origin,LatLng dest){
	   
       // Origin of route
       String str_origin = "origin="+origin.latitude+","+origin.longitude;
       //String str_origin = "origin="+srclat+","+srclng;
       // Destination of route
       String str_dest = "destination="+dest.latitude+","+dest.longitude;
       //String str_dest = "destination="+destlat+","+destlng;
       // Sensor enabled
       String sensor = "sensor=false";
       
    // Travelling Mode
       String mode = "mode=driving";
       String alternatives="alternatives=true";

       if(rbDriving.isChecked()){
           mode = "mode=driving";
           mMode = 0 ;
       }else if(rbBiCycling.isChecked()){
           mode = "mode=bicycling";
           mMode = 1;
       }else if(rbWalking.isChecked()){
           mode = "mode=walking";
           mMode = 2;
       }

       // Building the parameters to the web service
       String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&"+alternatives;
   
       
       // Output format
       String output = "json";

       // Building the url to the web service
       String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

       return url;
   }
   
   /** A method to download json data from url */
   private String downloadUrl(String strUrl) throws IOException{
       String data = "";
       InputStream iStream = null;
       HttpURLConnection urlConnection = null;
       try{
           URL url = new URL(strUrl);

           // Creating an http connection to communicate with url
           urlConnection = (HttpURLConnection) url.openConnection();

           // Connecting to url
           urlConnection.connect();

           // Reading data from url
           iStream = urlConnection.getInputStream();

           BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

           StringBuffer sb  = new StringBuffer();

           String line = "";
           while( ( line = br.readLine())  != null){
               sb.append(line);
           }

           data = sb.toString();

           br.close();

       }catch(Exception e){
           Log.d("Exception while downloading url", e.toString());
       }finally{
           iStream.close();
           urlConnection.disconnect();
       }
       return data;
   }

// Fetches data from url passed
   private class DownloadTask extends AsyncTask<String, Void, String>{

       // Downloading data in non-ui thread
       @Override
       protected String doInBackground(String... url) {

           // For storing data from web service
           String data = "";

           try{
               // Fetching the data from web service
               data = downloadUrl(url[0]);
           }catch(Exception e){
               Log.d("Background Task",e.toString());
           }
           return data;
       }

       // Executes in UI thread, after the execution of
       // doInBackground()
       @Override
       protected void onPostExecute(String result) {
           super.onPostExecute(result);

           ParserTask parserTask = new ParserTask();

           // Invokes the thread for parsing the JSON data
           parserTask.execute(result);
       }
   }
   

  
   /** A class to parse the Google Places in JSON format */
   private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
   	 ProgressDialog pDialog;
        @Override 
         protected void onPreExecute(){
       	 pDialog= new ProgressDialog(TestMapActivity.this);
       	 pDialog.setTitle("Please Wait");
            pDialog.setMessage("Plotting the Location");
            pDialog.show();
         } 

       // Parsing the data in non-ui thread
       @Override
       protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

           JSONObject jObject;
           List<List<HashMap<String, String>>> routes = null;

           try{
               jObject = new JSONObject(jsonData[0]);
               DirectionsJSONParser parser = new DirectionsJSONParser();

               // Starts parsing data
               routes = parser.parse(jObject);
           }catch(Exception e){
               e.printStackTrace();
           }
           return routes;
       }

       // Executes in UI thread, after the parsing process
       @Override
       protected void onPostExecute(List<List<HashMap<String, String>>> result) {
       	
           ArrayList<LatLng> points = null;
           PolylineOptions lineOptions = null;
           MarkerOptions markerOptions = new MarkerOptions();
           String distance = "";
           String duration = "";

           if(result.size()<1){
           	 pDialog.dismiss();
               Toast.makeText(getBaseContext(), "No Direction",Toast.LENGTH_LONG).show();
               return;
           }

           // Traversing through all the routes
           for(int i=0;i<result.size();i++){
               points = new ArrayList<LatLng>();
               lineOptions = new PolylineOptions();

               // Fetching i-th route
               List<HashMap<String, String>> path = result.get(i);
               if(result.size()<1){
                   Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                   return;
               }

               // Fetching all the points in i-th route
               for(int j=0;j<path.size();j++){
                   HashMap<String,String> point = path.get(j);

                   if(j==0){    // Get distance from the list
                       distance = (String)point.get("distance");
                       continue;
                   }else if(j==1){ // Get duration from the list
                       duration = (String)point.get("duration");
                       continue;
                   }

                   double lat = Double.parseDouble(point.get("lat"));
                   double lng = Double.parseDouble(point.get("lng"));
                   LatLng position = new LatLng(lat, lng);

                   points.add(position);
               }

               // Adding all the points in the route to LineOptions
               lineOptions.addAll(points);
               lineOptions.width(6);
               // Changing the color polyline according to the mode
               if(mMode==MODE_DRIVING)
                   lineOptions.color(Color.RED);             
               else if(mMode==MODE_BICYCLING)
               {
                   lineOptions.color(Color.GREEN);
                  
               }
               else if(mMode==MODE_WALKING)
                   lineOptions.color(Color.BLUE);
           }

           tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
           

           // Drawing polyline in the Google Map for the i-th route
           map.addPolyline(lineOptions);
           pDialog.dismiss();
       }
   }
  


}
