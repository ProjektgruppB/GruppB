package com.gruppb.maptest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.ProfilePictureView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	public static final String APP_ID = "122149494655808";
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
		    new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
		};					
	private Button mapBtn;
	private GraphUser user;
	private ProfilePictureView pic;
	private TextView userNameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);		 
		LoginButton authButton = (LoginButton) findViewById(R.id.login_button);
	    Session session = Session.getActiveSession();
		
		mapBtn = (Button) findViewById(R.id.showMap);
		userNameView = (TextView) findViewById(R.string.get_started);
		pic =  (ProfilePictureView) findViewById(R.id.picture_pic);
		pic.setCropped(true);
		
		mapBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				Intent myIntent = new Intent(v.getContext(), MapActivity.class);
				v.getContext().startActivity(myIntent);
			}
		});
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	   // if (requestCode == REAUTH_ACTIVITY_CODE) {
	    	uiHelper.onActivityResult(requestCode, resultCode, data);
	    	
	    //}
	}
	
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (session != null && state.isOpened()) {
	        mapBtn.setEnabled(true);
	        makeMeRequest(session);
	    
	    } else if (state.isClosed()) {
	        mapBtn.setEnabled(false);
	        pic.setProfileId(null);
	        ((TextView) findViewById(R.id.userwelcometext)).setText(R.string.get_started); 
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume(); 
		Session session = Session.getActiveSession();
		SessionState state = session.getState();
		if (!state.isOpened() ){
			mapBtn.setEnabled(false);
			pic.setProfileId(null);
			((TextView) findViewById(R.id.userwelcometext)).setText(R.string.get_started);
		}		
		else{
			makeMeRequest(session);
		}
	    //isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    //isResumed = false;
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser u, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (u != null) {
	                    // Set the id for the ProfilePictureView
	                    // view that in turn displays the profile picture.
	                    pic.setProfileId(u.getId());
	                    ((TextView) findViewById(R.id.userwelcometext)).setText("Welcome " + u.getName());
	                    user = u;
	                    
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 

}
