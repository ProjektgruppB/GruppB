package com.gruppb.maptest2;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gruppb.maptest2.GooglePlaces;
import com.gruppb.maptest2.MapActivity.LoadPlaces;
import com.gruppb.maptest2.AlertDialogManager;
import com.gruppb.maptest2.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MapActivity extends FragmentActivity{
    
	// Connection detector class
    private ConnectionDetector cd;
    
    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();
    
    // Places List
    private PlacesList nearPlaces;

    // Google Places
    private GooglePlaces googlePlaces;
    
    // Progress dialog
    private ProgressDialog pDialog;
     
    // ListItems data
    private ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
    private HashMap<Marker,String> markReference = new HashMap<Marker,String>();
    
    // Map
	private GoogleMap googlemap;
	
	// GPS Location
	private GPSTracker gpstracker;
	private LatLng mypos;
	
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
        cd = new ConnectionDetector(getApplicationContext());
        
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MapActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        else{
        	initMap();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	          case R.id.action_Legalnotices:
	        	  Intent intent = new Intent(MapActivity.this, LegalNoticesActivity.class);
	  			  startActivity(intent);
	              return true;

	          default:
	              return super.onOptionsItemSelected(item);
	      }
	  }

	public void initMap(){
		
		gpstracker = new GPSTracker(this);
        // check if GPS location can get
        if (gpstracker.canGetLocation()) {
        	
        	new LoadPlaces(gpstracker.getLatitude(),gpstracker.getLongitude()).execute();
	   		SupportMapFragment mapfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	   		googlemap = mapfragment.getMap();
	   		googlemap.setMyLocationEnabled(false);
	   		googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	   		
	   		//InfoWindow click listener
	   		googlemap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
	   			
	   			public void onInfoWindowClick(Marker marker) {
	   				String reference = markReference.get(marker);
	   				// Starting new intent
	                Intent in = new Intent(getApplicationContext(),
	                        PlaceActivity.class);
	                 
	                // Sending place refrence id to single place activity
	                // place refrence id used to get "Place full details"
	                in.putExtra(KEY_REFERENCE, reference);
	                startActivity(in);
	   			}

	   		});
	   		
	   		//MapClicklistener
	   		googlemap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng ll) {
					new LoadPlaces(ll.latitude,ll.longitude).execute();
				}
			});
	   		
	   		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	   		//Set camera
        	mypos = new LatLng(gpstracker.getLatitude(),gpstracker.getLongitude());
        	googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(mypos,(float)14.0));
        } else {
            // Can't get user's current location
            alert.showAlertDialog(MapActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
        	mypos = null;
            
        }
        

	}
    /**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {
 
    	private double Lat,Lng;
        public LoadPlaces(double cLat, double cLng) {
			Lat = cLat;
			Lng = cLng;
		}

		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Nearby Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		/**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();
             
            try {

                String types = "cafe|restaurant"; // Listing places only cafes, restaurants
                 
                // Radius in meters - increase this value if you don't find any places
                double radius = 1000; // 1000 meters 
                 
               
                //Get Places	
                nearPlaces = googlePlaces.search(Lat, Lng, radius, types);
              
                Log.w("Location:",String.valueOf(Lat) + String.valueOf(Lng));
                 
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;
                    Marker marker;
                     
                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                              
                                marker = googlemap.addMarker(new MarkerOptions()
                                .title(p.name)
                        		.snippet(p.vicinity)
                        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        		.position(new LatLng(p.geometry.location.lat,p.geometry.location.lng))
                                );
                                markReference.put(marker, p.reference);
                            }
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        alert.showAlertDialog(MapActivity.this, "Near Places",
                                "Sorry no places found.",
                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        alert.showAlertDialog(MapActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        alert.showAlertDialog(MapActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        alert.showAlertDialog(MapActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        alert.showAlertDialog(MapActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    }
                    else
                    {
                        alert.showAlertDialog(MapActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });
 
        }
 
    }

}
