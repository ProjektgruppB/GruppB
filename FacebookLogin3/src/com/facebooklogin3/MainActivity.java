package com.facebooklogin3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class MainActivity extends Activity implements OnClickListener {

	ImageView profile_pic, button;
	Facebook fb;
	private SharedPreferences sp;
	TextView welcome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String app_id = getString(R.string.app_id);
		fb = new Facebook(app_id);
		
		welcome = (TextView) findViewById(R.id.welcome); //?
		
		sp = getPreferences(MODE_PRIVATE);	//saves information to our phone
		String access_token= sp.getString("access_token", null); //searches for acess_token, if not then null
		long expires = sp.getLong("access_expires", 0);
		
		//If we did get the acess_token, everything saved in -> onComplete (cause then we know everything went out well)
		if(access_token != null){
			fb.setAccessToken(access_token);
		}
		//Keeps us logged in for a while
		if(expires != 0){
			fb.setAccessExpires(expires);
		}
		
		button = (ImageView)findViewById(R.id.login);
		profile_pic = (ImageView)findViewById(R.id.profile_pic);
		button.setOnClickListener(this);
		updateButtonImage();
	}

	private void updateButtonImage() {
		// Checks if you're either logged in or logged out and generates the right button for the page
		if(fb.isSessionValid()){
			button.setImageResource(R.drawable.com_facebook_loginbutton_silver);
			
			profile_pic.setVisibility(ImageView.VISIBLE);
			
			JSONObject obj = null;
			URL img_url = null;
			
			try {
				String jsonUser = fb.request("me");	//requests from user USING the app
				obj = Util.parseJson(jsonUser);
				String id = obj.optString("id");
				String name = obj.optString("name");
				welcome.setText("Welcome, " + name);	// Welcomes the user <name>
				img_url = new URL("http://graph.facebook.com/" + id + "/picture?type=small");	// gets us the profile pictue of the user
				Bitmap bmp = BitmapFactory.decodeStream(img_url.openConnection().getInputStream());
				profile_pic.setImageBitmap(bmp);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			button.setImageResource(R.drawable.com_facebook_loginbutton_blue);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		//Checks if we're logged in on facebook
		if(fb.isSessionValid()){
			//button will close the session - log out from facebook
			try {
				fb.logout(getApplicationContext());
				updateButtonImage();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//login to facebook
			fb.authorize(MainActivity.this, new String[]{"email"}, new DialogListener() {
				
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "fbError", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onComplete(Bundle values) {
					// We get the access_token, save the variable to a string called "access_token"
					// -> Loads up on our onCreate 
					Editor editor = sp.edit();
					editor.putString("access_token", fb.getAccessToken()); // getting the token info to SP
					editor.putLong("access_expires", fb.getAccessExpires()); // getting the expires info
					editor.commit(); // Saves everything to SharedPreferences
					updateButtonImage();
					 
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Ger resultat från "fb.authorize", ger resultat tillbaks. OBS - databasvärden?
		super.onActivityResult(requestCode, resultCode, data);
		fb.authorizeCallback(requestCode, resultCode, data);
	}
}
