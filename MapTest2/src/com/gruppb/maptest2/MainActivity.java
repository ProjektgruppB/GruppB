package com.gruppb.maptest2;

import com.gruppb.maptest2.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button mapBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mapBtn = (Button) findViewById(R.id.showMap); 
		mapBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				Intent myIntent = new Intent(v.getContext(), MapActivity.class);
				v.getContext().startActivity(myIntent);
			}
		});

	}
	
	public boolean onOptionItemSelected(MenuItem item){
		if (item.getItemId() == R.id.action_Legalnotices){
			startActivity(new Intent(this,LegalNoticesActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
