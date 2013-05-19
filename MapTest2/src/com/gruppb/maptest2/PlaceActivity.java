package com.gruppb.maptest2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
 
public class PlaceActivity extends Activity {
    // flag for Internet connection status
    Boolean isInternetPresent = false;
 
    // Connection detector class
    ConnectionDetector cd;
     
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
 
    // Google Places
    GooglePlaces googlePlaces;
     
    // Place Details
    PlaceDetails placeDetails;
     
    // Progress dialog
    ProgressDialog pDialog;
     
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);
         
        Intent i = getIntent();
        
        Button reviewBtn = (Button) findViewById(R.id.newreview); 
        TextView rateText = (TextView) findViewById(R.id.placeraterankTextView);
        RatingBar rateRatingBar = (RatingBar) findViewById(R.id.placerateratingbar);
        rateRatingBar.setEnabled(false);
        rateRatingBar.setRating((float) 1.5);
        //rateText.setVisibility(rateText.GONE);
        //rateRatingBar.setVisibility(rateRatingBar.GONE);
        reviewBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), ReviewActivity.class);
				myIntent.putExtra("place", placeDetails);
				v.getContext().startActivity(myIntent);
				
			}
		});
         
        // Place referece id
        String reference = i.getStringExtra(KEY_REFERENCE);
         
        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);
    }
     
     
    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlaceActivity.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];
             
            // creating Places class object
            googlePlaces = new GooglePlaces();
 
            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
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
                    if(placeDetails != null){
                        String status = placeDetails.status;
                         
                        // check place deatils status
                        // Check for all possible status
                        if(status.equals("OK")){
                            if (placeDetails.result != null) {
                                String name = placeDetails.result.name;
                                String icon = placeDetails.result.icon;
                                String address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                String longitude = Double.toString(placeDetails.result.geometry.location.lng);
                                 
                                Log.d("Place ", name + address + phone + latitude + longitude);
                                 
                                // Displaying all the details in the view
                                // place.xml
                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);
                                TextView lbl_location = (TextView) findViewById(R.id.location);
                                ImageView lbl_icon = (ImageView) findViewById(R.id.icon);
                                 
                                // Check for null data from google
                                // Sometimes place details might missing
                                name = name == null ? "Not present" : name; // if name is null display as "Not present"
                                icon = icon == null ? "Not present" : icon;
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;
                                 
                                
                                lbl_icon.setImageURI(Uri.parse(icon));
                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                                lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                            alert.showAlertDialog(PlaceActivity.this, "Near Places",
                                    "Sorry no place found.",
                                    false);
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                            alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                            alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                            alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                            alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        }
                        else
                        {
                            alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                    }else{
                        alert.showAlertDialog(PlaceActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                     
                     
                }
            });
 
        }
 
    }
 
}