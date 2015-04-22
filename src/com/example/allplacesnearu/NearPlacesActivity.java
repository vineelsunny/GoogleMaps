package com.example.allplacesnearu;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.app.ProgressDialog;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
 






import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 





import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
 
import java.util.ArrayList;

public class NearPlacesActivity extends ActionBarActivity {
	private String latitude;
    private String longitude;
    private String provider;
    Spinner sp12;
    private final String APIKEY ="AIzaSyD1qAgiN8O2D2RfR_6n45jJEuXESIz3eT4";
    private final int radius =1000;
    private  String type;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    Button btn;
    //accounting|airport|amusement_park|atm|bakery|bank|bar|beauty_salon|book_store|bus_station|cafe|car_dealer|car_rental|car_repair|car_wash|church|clothing_store|convenience_store|doctor|electronics_store|finance|fire_station|food|gas_station|grocery_or_supermarket|gym|health|hindu_temple|home_goods_store|hospital|jewelry_store|laundry|library|liquor_store|lodging|meal_delivery|movie_theater|park|pharmacy|place_of_worship|police|restaurant|school|shoe_store|shopping_mall|spa|train_station|travel_agency|university
   
    private ArrayList<Place> places;
    private ListView listView;
    MyLocation myLocation = new MyLocation();
    MyLocation.LocationResult locationResult;
    ProgressDialog progressDialog = null;
    String [] arr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httptestlist);
        arr=new String[]{"atm","food","bank"};
        btn=(Button)findViewById(R.id.findplacesbutton);
        sp12=(Spinner)findViewById(R.id.spinner12);
        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);	
        sp12.setAdapter(adapter12);
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        LocationListener myLocationListener = new MyLocationListener();
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 int selectedPosition = sp12.getSelectedItemPosition();
				 System.out.println("******position******"+selectedPosition);
				 type=arr[selectedPosition];
				 locationResult = new MyLocation.LocationResult() {
			            @Override
			            public void gotLocation(Location location) {
			                latitude = String.valueOf(location.getLatitude());
			                longitude = String.valueOf(location.getLongitude());
			                progressDialog.dismiss();
			                new GetCurrentLocation().execute(latitude, longitude); 
			            }
			        };
			       		     
			        MyRunnable myRun = new MyRunnable();
			        myRun.run();
			     
			        progressDialog = ProgressDialog.show(NearPlacesActivity.this, "Finding your location",
			                "Please wait...", true);
				
			}
        	
        });
     
        
        
    }
    
    @Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}
	
	
    public static Document loadXMLFromString(String xml) throws Exception {
    	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	  DocumentBuilder builder = factory.newDocumentBuilder();
    	 
    	  InputSource is = new InputSource(new StringReader(xml));
    	 
    	  return builder.parse(is);
    	}
    	 
    	private class GetCurrentLocation extends AsyncTask<Object, String, Boolean> {
    	 
    	  @Override
    	  protected Boolean doInBackground(Object... myLocationObjs) {
    	      if(null != latitude && null != longitude) {
    	          return true;
    	      } else {
    	          return false;
    	      }
    	  }
    	 
    	  @Override
    	  protected void onPostExecute(Boolean result) {
    	      super.onPostExecute(result);
    	      assert result;
    	      StringBuilder query = new StringBuilder();
    	      query.append("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?");
    	      query.append("location=" +  latitude + "," + longitude + "&");
    	      query.append("radius=" + radius + "&");
    	      query.append("types=" + type + "&");
    	      System.out.println("********type="+type);
    	      query.append("sensor=true&"); //Must be true if queried from a device with GPS
    	      query.append("key=" + APIKEY);
    	      new QueryGooglePlaces().execute(query.toString());
    	  }
    	}
    	private class QueryGooglePlaces extends AsyncTask<String, String, String> {
    		 
    	    @Override
    	    protected String doInBackground(String... args) {
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpResponse response;
    	        String responseString = null;
    	        try {
    	            response = httpclient.execute(new HttpGet(args[0]));
    	            StatusLine statusLine = response.getStatusLine();
    	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	                ByteArrayOutputStream out = new ByteArrayOutputStream();
    	                response.getEntity().writeTo(out);
    	                out.close();
    	                responseString = out.toString();
    	            } else {
    	                //Closes the connection.
    	                response.getEntity().getContent().close();
    	                throw new IOException(statusLine.getReasonPhrase());
    	            }
    	        } catch (ClientProtocolException e) {
    	            Log.e("ERROR", e.getMessage());
    	        } catch (IOException e) {
    	            Log.e("ERROR", e.getMessage());
    	        }
    	        return responseString;
    	    }
    	    @Override
    	    protected void onPostExecute(String result) {
    	        super.onPostExecute(result);
    	        try {
    	            Document xmlResult = loadXMLFromString(result);
    	            NodeList nodeList =  xmlResult.getElementsByTagName("result");
    	            places=new ArrayList<Place>();
    	            for(int i = 0, length = nodeList.getLength(); i < length; i++) {
    	                
    	                Node node = nodeList.item(i);
    	                
    	                if(node.getNodeType() == Node.ELEMENT_NODE) {
    	                    Element nodeElement = (Element) node;
    	                    Place place = new Place();
    	                    
    	                    Node name = nodeElement.getElementsByTagName("name").item(0);
    	                    Node vicinity = nodeElement.getElementsByTagName("vicinity").item(0);
    	                    Node rating = nodeElement.getElementsByTagName("rating").item(0);
    	                    Node reference = nodeElement.getElementsByTagName("reference").item(0);
    	                    Node id = nodeElement.getElementsByTagName("id").item(0);
    	                    Node geometryElement = nodeElement.getElementsByTagName("geometry").item(0);
    	                    
    	                    NodeList locationElement = geometryElement.getChildNodes();
    	                    
    	                    Element latLngElem = (Element) locationElement.item(1);
    	                    
    	                    Node lat = latLngElem.getElementsByTagName("lat").item(0);
    	                    Node lng = latLngElem.getElementsByTagName("lng").item(0);
    	                    
    	                    float[] geometry =  {Float.valueOf(lat.getTextContent()),
    	                            Float.valueOf(lng.getTextContent())};
    	                            
    	                    int typeCount = nodeElement.getElementsByTagName("type").getLength();
    	                    
    	                    String[] types = new String[typeCount];
    	                    
    	                    for(int j = 0; j < typeCount; j++) {
    	                        types[j] = nodeElement.getElementsByTagName("type").item(j).getTextContent();
    	                    }
    	                    
    	                    place.setVicinity(vicinity.getTextContent());
    	                    place.setId(id.getTextContent());
    	                    place.setName(name.getTextContent());
    	                    
    	                    if(null == rating) {
    	                        place.setRating(0.0f);
    	                    } else {
    	                        place.setRating(Float.valueOf(rating.getTextContent()));
    	                    }
    	                    
    	                    place.setReference(reference.getTextContent());
    	                    place.setGeometry(geometry);
    	                    place.setTypes(types);
    	                    places.add(place);
    	                }
    	            }
    	            
    	            PlaceAdapter placeAdapter = new PlaceAdapter(NearPlacesActivity.this, R.layout.httptestrow, places);
    	            
    	            listView = (ListView)findViewById(R.id.httptestlist_listview);
    	            listView.setAdapter(placeAdapter);
    	 
    	        } catch (Exception e) {
    	            Log.e("ERROR", e.getMessage());
    	        }
    	    }
    	}
    	private class PlaceAdapter extends ArrayAdapter<Place> {
    	    public Context context;
    	    public int layoutResourceId;
    	    public ArrayList<Place> places;
    	 
    	    public PlaceAdapter(Context context, int layoutResourceId, ArrayList<Place> places) {
    	        super(context, layoutResourceId, places);
    	        this.layoutResourceId = layoutResourceId;
    	        this.places = places;
    	    }
    	 
    	    @Override
    	    public View getView(int rowIndex, View convertView, ViewGroup parent) {
    	        View row = convertView;
    	        if(null == row) {
    	            LayoutInflater layout = (LayoutInflater)getSystemService(
    	                    Context.LAYOUT_INFLATER_SERVICE
    	            );
    	            row = layout.inflate(R.layout.httptestrow, null);
    	        }
    	        Place place = places.get(rowIndex);
    	        if(null != place) {
    	            TextView name = (TextView) row.findViewById(R.id.htttptestrow_name);
    	            TextView vicinity = (TextView) row.findViewById(
    	                    R.id.httptestrow_vicinity);
    	            if(null != name) {
    	                name.setText(place.getName());
    	            }
    	            if(null != vicinity) {
    	                vicinity.setText(place.getVicinity());
    	            }
    	        }
    	        return row;
    	    }
    	}
    	private class MyLocationListener implements LocationListener {
    		 
    	    @Override
    	    public void onLocationChanged(Location location) {
    	        latitude = String.valueOf(location.getLatitude());
    	        longitude = String.valueOf(location.getLongitude());
    	    }
    	 
    	    @Override
    	    public void onStatusChanged(String s, int i, Bundle bundle) {
    	    }
    	 
    	    @Override
    	    public void onProviderEnabled(String s) {
    	    }
    	 
    	    @Override
    	    public void onProviderDisabled(String s) {
    	    }
    	}
    	
    	public class MyRunnable implements Runnable {
            public MyRunnable() {
            }
     
            public void run() {
                myLocation.getLocation(getApplicationContext(), locationResult);
            }
        }
    }


