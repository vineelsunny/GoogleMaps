package com.example.allplacesnearu;

import android.graphics.Color;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;

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

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
public class KleemMapActivity  extends FragmentActivity implements LocationListener {
	
	GoogleMap googleMap;
	 private static final LatLng Ameerpet = new LatLng(17.435978600000000000,78.448195599999960000);
	 private static final LatLng yousufguda = new LatLng(17.435332800000000000,78.435711800000030000);
	 private static final LatLng srnagar = new LatLng(17.443649700000000000,78.445825900000050000);
	 TextView tvLocation;
	 
	
	static int count=0;
	 ArrayList<LatLng> delivaryPositions;
	@Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.find_me);
       tvLocation = (TextView) findViewById(R.id.tv_location);
       
       int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
       if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        
           int requestCode = 10;
           Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
           dialog.show();

       }else { // Google Play Services are available
           SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           googleMap = fm.getMap();
           googleMap.setMyLocationEnabled(true);
           LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           Criteria criteria = new Criteria();
           String provider = locationManager.getBestProvider(criteria, true);
           Location location = locationManager.getLastKnownLocation(provider);
           if(location!=null){
               onLocationChanged(location);
           }
           locationManager.requestLocationUpdates(provider, 20000, 0, this);
       }
   }
	
   @Override
   public void onLocationChanged(Location location) {
	   delivaryPositions=new ArrayList<LatLng>();
       double latitude = location.getLatitude();
       double longitude = location.getLongitude();
       LatLng clatLng = new LatLng(latitude, longitude);
       delivaryPositions.add(clatLng);  
		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(clatLng,10);	  
       Marker hyd = googleMap.addMarker(new MarkerOptions().position(clatLng)); 
       googleMap.animateCamera(update);
       new PlotPositions().execute();
      

   }

   @Override
   public void onProviderDisabled(String provider) {
       // TODO Auto-generated method stub
   }

   @Override
   public void onProviderEnabled(String provider) {
       // TODO Auto-generated method stub
   }

   @Override
   public void onStatusChanged(String provider, int status, Bundle extras) {
       // TODO Auto-generated method stub
   }
   
   public class PlotPositions extends AsyncTask<Void, Void, String>
   {

	@Override
	protected String doInBackground(Void... params) {
		String str="hello";
		return str;
	}
	  @Override
	  protected void onPostExecute(String s)
	  {
		  delivaryPositions.add(Ameerpet);
		  delivaryPositions.add(yousufguda);
		  delivaryPositions.add(srnagar);
		  for(LatLng ln:delivaryPositions)
		  {
			  Marker hw=googleMap.addMarker(new MarkerOptions().position(ln));
			  hw.getPosition();
		  }
		  googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
		  plotRouteOnMap(delivaryPositions);
	  }
   }
   
   
  public void  plotRouteOnMap(ArrayList<LatLng> delP)
   {
	  LatLng current=delP.get(0);
	 LatLng dest=delP.get(2);
	  
	
	
      String url =getDirectionsUrl(current,dest);
      System.out.println("Url*****"+url);
      DownloadTask downloadTask =new DownloadTask();
      downloadTask.execute(url); 
      
   }
  
  


  
 /* public void getDistanceAndPlot(LatLng ln1,LatLng ln2){
	  String url = getDirectionsUrl(ln1,ln2);
      DownloadTask downloadTask = new DownloadTask();
      downloadTask.execute(url); 
	  
  }*/
  
  private String getDirectionsUrl(LatLng origin,LatLng dest){
      String str_origin = "origin="+origin.latitude+","+origin.longitude;
      String str_dest = "destination="+dest.latitude+","+dest.longitude;
      String sensor = "sensor=false";
      String mode = "mode=driving";
      String alternatives="alternatives=true";
      String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&"+alternatives;
      String output = "json";
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
          urlConnection = (HttpURLConnection) url.openConnection();
          urlConnection.connect();
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

      @Override
      protected String doInBackground(String... url) {
          String data = "";
          try{
              
              data = downloadUrl(url[0]);
          }catch(Exception e){
              Log.d("Background Task",e.toString());
          }
          return data;
      }

     
      @Override
      protected void onPostExecute(String result) {
          super.onPostExecute(result);
          ParserTask parserTask = new ParserTask();        
          parserTask.execute(result);
      }
  }
  
 
  private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
  	 ProgressDialog pDialog;
       @Override 
        protected void onPreExecute(){
      	 pDialog= new ProgressDialog(KleemMapActivity.this);
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
              System.out.println("Routes***"+routes.toString());            
          }catch(Exception e){
              e.printStackTrace();
          }
          return routes;
      }

      
      @Override
      protected void onPostExecute(List<List<HashMap<String, String>>> result) {
      	
          ArrayList<LatLng> points = null;
          PolylineOptions lineOptions = null;
       
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
              
              System.out.println("path***"+path.toString());
              
              if(result.size()<1){
                  Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                  return;
              }
              // Fetching all the points in i-th route
              for(int j=0;j<path.size();j++){
            	  
                  HashMap<String,String> point = path.get(j);
                  
                  System.out.println("point***"+point.toString());
                  
                  if(j==0){    // Get distance from the list
                      distance = (String)point.get("distance");

                      String [] arr=distance.split(" ");   
                      double d=Double.parseDouble(arr[0]);
                      System.out.println("Distance**"+d);
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
              lineOptions.color(Color.BLUE);
              if(count==0)
              {
              googleMap.addPolyline(lineOptions);
              count++;
              }
              
          }
          tvLocation.setText("Distance:"+distance + ", Duration:"+duration);
          // Drawing polyline in the Google Map for the i-th route
          //googleMap.addPolyline(lineOptions);
          pDialog.dismiss();
          count=0;
      }
  }
  
}

