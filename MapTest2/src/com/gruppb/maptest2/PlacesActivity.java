package com.gruppb.maptest2;

import java.util.HashMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class PlacesActivity extends Activity {

    // Places Listview
    ListView lv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places);
	}

	/*
	// Successfully got places details
    if (nearPlaces.results != null) {
        // loop through each place
        for (Place p : nearPlaces.results) {
            HashMap<String, String> map = new HashMap<String, String>();
             
            // Place reference won't display in listview - it will be hidden
            // Place reference is used to get "place full details"
            map.put(KEY_REFERENCE, p.reference);
             
            // Place name
            map.put(KEY_NAME, p.name);
             
             
            // adding HashMap to ArrayList
            placesListItems.add(map);
            
            marker = new MarkerOptions()
    		.title(p.name)
    		.snippet(p.vicinity)
    		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
    		.position(new LatLng(p.geometry.location.lat,p.geometry.location.lng));
            googlemap.addMarker(marker);
        }
        // list adapter
        /*ListAdapter adapter = new SimpleAdapter(MapActivity.this, placesListItems,
                R.layout.list_item,
                new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                        R.id.reference, R.id.name });
         
        // Adding data into listview
        lv.setAdapter(adapter);
       
    }
}*/

}
