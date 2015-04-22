package com.example.allplacesnearu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 









import org.json.JSONObject;
 









import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
 









import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionActivity extends FragmentActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        findsrcdest=(Button)findViewById(R.id.findsrcdest);
        
       findsrcdest.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			markerPoints.clear();		//so that the array will be clear wen v try to locate for the second time	
			Intent intent=new Intent(DirectionActivity.this,DirectionIntro.class);
			startActivityForResult(intent,2);
			
		}
    	   
       });
    
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
        //map.clear();
        
        // Setting onclick event listener for the map
        /*map.setOnMapClickListener(new OnMapClickListener() {
 
            @Override
            public void onMapClick(LatLng point) {
 
                // Already two locations
                if(markerPoints.size()>1){
                    markerPoints.clear();
                    //map.clear();
                }
 
                // Adding new item to the ArrayList
                markerPoints.add(point);
 
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
 
                // Setting the position of the marker
                options.position(point);
 
                /**
                * For the start location, the color of marker is GREEN and
                * for the end location, the color of marker is RED.
                
                if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else if(markerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
 
                // Add new marker to the Google Map Android API V2
                map.addMarker(options);
 
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
        });*/
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
                  // fetch the message String
                  srclat=data.getDoubleExtra("srclat",-33.8600);
                  srclng=data.getDoubleExtra("srclng",151.2094);
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
        	 pDialog= new ProgressDialog(DirectionActivity.this);
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
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
