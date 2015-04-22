package com.example.allplacesnearu;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
public class KleemAppTestActivity  extends FragmentActivity implements LocationListener,OnMarkerClickListener {
	
	GoogleMap googleMap;
	 private static final LatLng Ameerpet = new LatLng(17.435978600,78.448195599);
	 private static final LatLng yousufguda = new LatLng(17.435332800,78.435711800);
	 private static final LatLng srnagar = new LatLng(17.44364970,78.445825900);
	 TextView tvLocation;
	static double Min=0.0;
    static int positionNo; 
	static int count=0;
	SharedPreferences spf;
	String filename="PositionNo";
	 ArrayList<LatLng> delivaryPositions;
	 ArrayList<Marker> markers;
	 PolylineOptions lineOptions = null;
	 ArrayList<Bitmap> arbms=null;
	 Button animate;
	 ArrayList<LatLng> zoomInPositions=null;
	 int currentPt;
	@Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.find_me);
       tvLocation = (TextView) findViewById(R.id.tv_location);
       animate=(Button)findViewById(R.id.button_animate);
       int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
       if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        
           int requestCode = 10;
           Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
           dialog.show();

       }else { // Google Play Services are available
           SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           googleMap = fm.getMap();
           googleMap.setMyLocationEnabled(true);
           googleMap.getUiSettings().setZoomControlsEnabled(true);
           googleMap.getUiSettings().setCompassEnabled(true);
           googleMap.getUiSettings().setMyLocationButtonEnabled(true);
           googleMap.getUiSettings().setAllGesturesEnabled(true);
           googleMap.setOnMarkerClickListener(this);
           LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           Criteria criteria = new Criteria();
           String provider = locationManager.getBestProvider(criteria, true);
           
           Location location = locationManager.getLastKnownLocation(provider);
           
           if(location!=null){
        	   
               onLocationChanged(location);
         
               delivaryPositions=new ArrayList<LatLng>();
               double latitude = location.getLatitude();
               double longitude = location.getLongitude();
               LatLng clatLng = new LatLng(latitude, longitude);
               delivaryPositions.add(clatLng);  
        			  
               Marker hyd1 = googleMap.addMarker(new MarkerOptions().position(clatLng)); 
               markers=new ArrayList<Marker>();
               markers.add(hyd1);
               new PlotPositions().execute();
               
           }
           locationManager.requestLocationUpdates(provider, 20000, 0, this);
          
       }
       
   }
	
	
   @Override
   public void onLocationChanged(Location location) {
	  /* delivaryPositions=new ArrayList<LatLng>();
       double latitude = location.getLatitude();
       double longitude = location.getLongitude();
       LatLng clatLng = new LatLng(latitude, longitude);
       delivaryPositions.add(clatLng);  
			  
       Marker hyd1 = googleMap.addMarker(new MarkerOptions().position(clatLng)); 
       markers=new ArrayList<Marker>();
       markers.add(hyd1);
       new PlotPositions().execute();*/
      

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
	   public void onPreExecute()
	   {
		      delivaryPositions.add(Ameerpet);
			  delivaryPositions.add(yousufguda);
			  delivaryPositions.add(srnagar);
	   }

	@Override
	protected String doInBackground(Void... params) {
		arbms=new ArrayList<Bitmap>();
		for(int i=1;i<=delivaryPositions.size();i++)
		{
			try
			{
				String url="http://www.googlemapsmarkers.com/v1/"+i+"/0099FF/";
		         Bitmap bm=BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
		         arbms.add(bm);
			}catch(Exception e)
			{
				
			}
			 
		}
		
		String str="hello";
		return str;
	}
	  @Override
	  protected void onPostExecute(String s)
	  {
		 // int count=0;
		  /*for(LatLng ln:delivaryPositions)
		  {
			 //Bitmap bm1=arbms.get(count);
              Marker hyd=googleMap.addMarker(new MarkerOptions().position(ln));//.icon(BitmapDescriptorFactory.fromBitmap(bm1)));

			 markers.add(hyd);
			 System.out.println("%%"+hyd.getPosition());
			System.out.println("%%="+hyd.getId());
			//count++;
		  }
		   zoomIn(markers);*/
		  plotRouteOnMap(delivaryPositions);
		  // plotMap(delivaryPositions,0,delivaryPositions.size());
	  }
   }
   

   public void zoomIn(ArrayList<Marker> mark)
   {
	   
	   LatLngBounds.Builder b = new LatLngBounds.Builder();
	   for (Marker m : mark) {
	       b.include(m.getPosition());
	   } 
	   LatLngBounds bounds = b.build();
	   //Change the padding as per needed 
	   CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 275,275,15);
	   googleMap.animateCamera(cu);
	
   }
   
   
  public void  plotRouteOnMap(ArrayList<LatLng> delP)
   {
	  
	ArrayList<LatLng> newPositions=new ArrayList<LatLng>();
	
	ArrayList<BeanSort> bs=new ArrayList<BeanSort>();
	
	 LatLng current=delP.get(0);
	 
	  float src_lat=(float)current.latitude;
	  float src_lng=(float)current.longitude;
	  
	  newPositions.add(current);
	  
	  for(int j=1;j<delP.size();j++)
	  {
			  LatLng dest=delP.get(j);
			  float dest_lat=(float)dest.latitude;
			  float dest_lng=(float)dest.longitude;
			  double distance=distFrom(src_lat,src_lng,dest_lat,dest_lng);			  
			  System.out.println("0->"+j+"%%"+distance);
              bs.add(new BeanSort("alan",dest,distance));
	  }
	
	  Collections.sort(bs,new BeanSort());
	  
	  for(BeanSort temp: bs){
		  System.out.println("%%hello in collection");
		  LatLng ln=temp.getLatlng();
		  newPositions.add(ln);
		  //double lat=ln.latitude;
		 // double lng=ln.longitude;
		   //System.out.println("^^"+temp.getDistance()+" "+"lat"+lat+" "+lng);
		}
	  zoomInPositions=new ArrayList<LatLng>();
	  zoomInPositions=newPositions;
	  MarkerWithImages(newPositions);
	  sendNewPositions(newPositions);
	 
	  //for testing
	 /* LatLng curr=delP.get(3);
	  LatLng dest3=delP.get(0);
	  String url =getDirectionsUrl(curr,dest3);				     
      DownloadTask downloadTask =new DownloadTask();
      downloadTask.execute(url);*/
      
      //new one
	 /* for(int i=0;i<newPosition.size();i++)
	  {
		  LatLng curr=newPosition.get(i);
		
		  if(i==(newPosition.size()-1))
		  {
			  LatLng dest12=newPosition.get(0);
			 String url =getDirectionsUrl(curr,dest12);				     
		      DownloadTask downloadTask =new DownloadTask();
		      downloadTask.execute(url);
		      newPositions.clear();	
		  }
		  else
		  {
			  LatLng dest12=newPosition.get(i+1);
			  String url =getDirectionsUrl(curr,dest12);				     
		      DownloadTask downloadTask =new DownloadTask();
		      downloadTask.execute(url);
		
		  }
		  
	  }*/
	 
	  //calculate the distance from 1 point to next and so on
	  
	 /* for(int i=0;i<delP.size();i++)
	  {
		  LatLng curr=delP.get(i);
		  float src_l=(float)curr.latitude;
		  float src_ln=(float)curr.longitude;
		  
		  if(i==(delP.size()-1))
		  {
			  LatLng dest12=delP.get(0);
			  float dest_l=(float)dest12.latitude;
			  float dest_ln=(float)dest12.longitude;
			  float distance=distFrom(src_l,src_ln,dest_l,dest_ln);
			  System.out.println(i+"%%"+(i+1)+" "+distance);
		  }
		  else
		  {
			  LatLng dest12=delP.get(i+1);
			  float dest_l=(float)dest12.latitude;
			  float dest_ln=(float)dest12.longitude;
			  float distance=distFrom(src_lat,src_lng,dest_l,dest_ln);
			  System.out.println(i+"%%"+(i+1)+" "+distance);
		  }
		 
		
	  }*/
	 
	 
	  /*for(int i=0;i<delP.size();i++)
	  {
		  LatLng current=delP.get(i);			 
			 double src_l=current.latitude;
			 String src_lat=String.valueOf(src_l);
			 System.out.println("@@@SRC_working"+src_lat);
			 double src_ln=current.longitude;
			 String src_lng=String.valueOf(src_ln);

			for(int j=0;j<delP.size();j++)
			{
				if(i!=j)
				{
					LatLng dest=delP.get(j);
					
					 double dest_l=dest.latitude;
					 String dest_lat=String.valueOf(dest_l);
					 
					 double dest_ln=dest.longitude;
					 String dest_lng=String.valueOf(dest_ln);
					 
				     calculate_distance(src_lat, src_lng, dest_lat, dest_lng,i);
				    
				}
					
				 if(j+1==delP.size())
			     {
			    	 spf=getSharedPreferences(filename, 0);
					  int k=spf.getInt("positionNumber",2);
						String url =getDirectionsUrl(current,delP.get(k));
					     
					      DownloadTask downloadTask =new DownloadTask();
					      downloadTask.execute(url);
			     }
				
				
			}
			
	  } */
	  
	 /* LatLng current=delP.get(0);
		 
		 
		 double src_l=current.latitude;
		 String src_lat=String.valueOf(src_l);
		 System.out.println("@@@SRC_working"+src_lat);
		 double src_ln=current.longitude;
		 String src_lng=String.valueOf(src_ln);
		 
		 

		for(int i=0;i<delP.size();i++)
		{
			
				LatLng dest=delP.get(i);
				
				 double dest_l=dest.latitude;
				 String dest_lat=String.valueOf(dest_l);
				 
				 double dest_ln=dest.longitude;
				 String dest_lng=String.valueOf(dest_ln);
				 
			     calculate_distance(src_lat, src_lng, dest_lat, dest_lng,i);
			     if(i+1==delP.size())
			     {
			    	 spf=getSharedPreferences(filename, 0);
					  int k=spf.getInt("positionNumber",2);
						String url =getDirectionsUrl(current,delP.get(k));				     
					      DownloadTask downloadTask =new DownloadTask();
					      downloadTask.execute(url);		
				
			}
				
			
		}*/

  
   }
  
  public void MarkerWithImages(ArrayList<LatLng> newPosition)
  {
	  googleMap.clear();
	  int count=0;
	  for(LatLng ln:newPosition)
	  {
		 Bitmap bm1=arbms.get(count);
          Marker hyd=googleMap.addMarker(new MarkerOptions().position(ln).icon(BitmapDescriptorFactory.fromBitmap(bm1)));
          markers.add(hyd);
		count++;
	  }
	  zoomIn(markers);
  }
  public void sendNewPositions(ArrayList<LatLng> newPosition)
  {
	  //tested working
	  /*for(int i=0;i<newPosition.size();i++)
	  {
		  double lat=newPosition.get(i).latitude;
		  double lng=newPosition.get(i).longitude;
		  System.out.println("##"+i+" "+"lat"+lat+" "+lng);
	  }
	  
	  LatLng curr=newPosition.get(0);
	  LatLng dest3=newPosition.get(3);
	  String url =getDirectionsUrl(curr,dest3);				     
      DownloadTask downloadTask =new DownloadTask();
      downloadTask.execute(url);*/
	  
	  for(int i=0;i<newPosition.size();i++)
	  {
		  LatLng curr=newPosition.get(i);
		  
     if(i<3)
    {
	LatLng dest12=newPosition.get(i+1);
	  String url =getDirectionsUrl(curr,dest12);				     
    DownloadTask downloadTask =new DownloadTask();
    downloadTask.execute(url);
    }
	if(i==3)
	{
		LatLng dest12=newPosition.get(0);
		  String url =getDirectionsUrl(curr,dest12);				     
	    DownloadTask downloadTask =new DownloadTask();
	    downloadTask.execute(url);
	}
		
		  
		  
	  }

  }
  public void Animate(View v)
  {
	  if(zoomInPositions==null || zoomInPositions.isEmpty()){
	      Toast.makeText(getApplicationContext(), 
	        "Not enought point!", 
	        Toast.LENGTH_LONG).show();
	     }else{

	      float zoomValue =12.5f;
	      
	      googleMap.animateCamera(
	        CameraUpdateFactory.zoomTo(zoomValue),5000,MyCancelableCallback);      
	      
	      currentPt = 0-1;

	     }
	    
  }
  CancelableCallback MyCancelableCallback =new CancelableCallback(){
	  @Override
	    public void onCancel() {
	   
	    }

	    @Override
	    public void onFinish() {
	    	 if(++currentPt < zoomInPositions.size()){
	    		 
	    		/* googleMap.animateCamera(
	    			        CameraUpdateFactory.newLatLng(zoomInPositions.get(currentPt)), 
	    			        5000,
	    			        MyCancelableCallback);*/
	    	      
	    	     //Get the current location
	    	      Location startingLocation = new Location("starting point");
	    	      startingLocation.setLatitude(googleMap.getCameraPosition().target.latitude);
	    	      startingLocation.setLongitude(googleMap.getCameraPosition().target.longitude);
	    	      
	    	      //Get the target location
	    	      Location endingLocation = new Location("ending point");
	    	      endingLocation.setLatitude(zoomInPositions.get(currentPt).latitude);
	    	      endingLocation.setLongitude(zoomInPositions.get(currentPt).longitude);
	    	      
	    	      //Find the Bearing from current location to next location
	    	      float targetBearing = startingLocation.bearingTo(endingLocation);
	    	      
	    	      LatLng targetLatLng = zoomInPositions.get(currentPt);
	    	      float targetZoom =17.5f;
	    	      
	    	      //Create a new CameraPosition
	    	      CameraPosition cameraPosition =
	    	        new CameraPosition.Builder()
	    	          .target(targetLatLng)
	    	                         .bearing(targetBearing)
	    	                         .zoom(targetZoom)
	    	                         .build();
	    	 
	    	      
	    	      googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),5000,MyCancelableCallback);

	    	      
	    	     }else{
	    	     
	    	     }
	    }
  };
  //recursive function
  
  /*public void plotMap(ArrayList<LatLng> arr,int i,int j)
  {
	  LatLng current=arr.get(i);
	  double src_l=current.latitude;
		 String src_lat=String.valueOf(src_l);
		 System.out.println("@@@SRC_working"+src_lat);
		 double src_ln=current.longitude;
		 String src_lng=String.valueOf(src_ln);
		 
	  for(int k=i;k<j;k++)
	  {
		  if(i!=k)
			{
				LatLng dest=arr.get(k);
				
				 double dest_l=dest.latitude;
				 String dest_lat=String.valueOf(dest_l);
				 
				 double dest_ln=dest.longitude;
				 String dest_lng=String.valueOf(dest_ln);
				 
			     calculate_distance(src_lat, src_lng, dest_lat, dest_lng,i);
			     if(k+1==arr.size())
			     {
			    	 spf=getSharedPreferences(filename, 0);
					  int pos=spf.getInt("positionNumber",2);
						String url =getDirectionsUrl(current,arr.get(pos));
					      DownloadTask downloadTask =new DownloadTask();
					      downloadTask.execute(url);
					      plotMap(arr,i+1,j);
			     }
			}
	  }
  }*/
//end
  
  private String getDirectionsUrl(LatLng origin,LatLng dest){
      String str_origin = "origin="+origin.latitude+","+origin.longitude;
      String str_dest = "destination="+dest.latitude+","+dest.longitude;
      String sensor = "sensor=false";
      String mode = "mode=driving";
     // String alternatives="alternatives=true";
      String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
      String output = "json";
      String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
      return url;
  }
  

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
      	 pDialog= new ProgressDialog(KleemAppTestActivity.this);
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
          //PolylineOptions lineOptions = null;
       
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
  
  
 /* public void calculate_distance(String src_lat, String src_lng, String dest_lat, String dest_lng,int i)
  {
	  System.out.println("@@IN caluculate distance method");
	  
	 
      //distance = getdistance(makeUrl(src_lat, src_lng, dest_lat, dest_lng));
	  String url=makeUrl(src_lat, src_lng, dest_lat, dest_lng);
	  String [] arr={url,String.valueOf(i)};
	  new GetDistance().execute(arr);
     
  } 
  
  private String makeUrl(String src_lat, String src_lng, String dest_lat, String dest_lng) {
	  
	 
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://maps.googleapis.com/maps/api/distancematrix/json?");
	    urlString.append("origins="); // from
	    urlString.append(src_lat);
	    urlString.append(",");
	    urlString.append(src_lng);
	    urlString.append("&destinations="); // to
	    urlString.append(dest_lat);
	    urlString.append(",");
	    urlString.append(dest_lng);
	    urlString.append("&sensor=true&mode=driving");
	    System.out.println("@@@URL"+urlString.toString());
	    
	    return urlString.toString();
	} 
  class GetDistance extends AsyncTask<String, Void, String[]>
  {

	@Override
	protected String[] doInBackground(String... params) {
		 URLConnection urlConnection = null;
	     URL url = null;
	     try { 
	    	 System.out.println(params[0]);
	          url = new URL(params[0]);
	          urlConnection = url.openConnection();
	          BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	          String line;
	          StringBuffer sb = new StringBuffer();
	          // take Google's legible JSON and turn it into one big string. 
	          while ((line = in.readLine()) != null) 
	          {
	                sb.append(line);
	          } 
	          // turn that string into a JSON object 
	          JSONObject distanceMatrixObject = new JSONObject(sb.toString());
	         
	          // now get the JSON array that's inside that object 
	          if (distanceMatrixObject.getString("status").equalsIgnoreCase("OK"))
	          {
	        	  String distance;
	        	  System.out.println("@@In get Distance Method***");
	                JSONArray distanceArray = new JSONArray(distanceMatrixObject.getString("rows"));
	                
	                JSONArray elementsArray = new JSONArray(distanceArray.getJSONObject(0).getString("elements"));
	                
	                JSONObject distanceObject = new JSONObject(elementsArray.getJSONObject(0).getString("distance"));
	                
	              
	                
	                distance=distanceObject.getString("text");
	                
	                System.out.println("@@DistanceText"+distance);
	                
	                String arrr[]={distance,params[1]};
	                
	               // return distanceObject.getString("text");
	                return arrr;
	          }
	 	      
	     }
	     catch (Exception e) {
	          e.printStackTrace();
	          
	     } 
		return null;
	}
	@Override
	public void onPostExecute(String[] arr){
		String distance=arr[0];
		String i=arr[1];
		String [] arrr=distance.split(" ");
		spf=getSharedPreferences(filename, 0);
		SharedPreferences.Editor editor=spf.edit();
		double dist=Double.parseDouble(arrr[0]);
		if(Min==0.0)
		{
			Min=dist;
			editor.putInt("positionNumber",Integer.parseInt(arr[1]));
			editor.commit();
			positionNo=Integer.parseInt(arr[1]);
			
		}
		else if(Min>dist)
		{
			Min=dist;
			editor.putInt("positionNumber",Integer.parseInt(arr[1]));
			editor.commit();
			positionNo=Integer.parseInt(arr[1]);
		}
			
		System.out.println("@@Min"+Min+"@@pos"+positionNo);
		
	}
	  
  }*/
  
  public static double distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	 
	    int meterConversion = 1609;
	 
	    return (dist * meterConversion);
	}


@Override
public boolean onMarkerClick(final Marker marker) {
	 //Make the marker bounce
    final Handler handler = new Handler();
    
    final long startTime = SystemClock.uptimeMillis();
    final long duration = 2000;
    
    Projection proj = googleMap.getProjection();
    final LatLng markerLatLng = marker.getPosition();
    Point startPoint = proj.toScreenLocation(markerLatLng);
    startPoint.offset(0, -100);
    final LatLng startLatLng = proj.fromScreenLocation(startPoint);

    final Interpolator interpolator = new BounceInterpolator();

    handler.post(new Runnable() {
        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - startTime;
            float t = interpolator.getInterpolation((float) elapsed / duration);
            double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
            double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
            marker.setPosition(new LatLng(lat, lng));

            if (t < 1.0) {
                // Post again 16ms later.
                handler.postDelayed(this, 16);
            }
        }
    });
    
    //return false; //have not consumed the event
    return true; //have consumed the event
	
}
  
  
}

