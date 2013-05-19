package com.gruppb.maptest2;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog;

public class ReviewActivity extends Activity {

	private Button submitBtn;
	private RatingBar ratebar;
	private PlaceDetails place;
	
	// Parameters of a WebDialog that should be displayed
    private WebDialog dialog = null;
    private String dialogAction = null;
    private Bundle dialogParams = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);
		
		Intent i = getIntent();
		place = (PlaceDetails) i.getSerializableExtra("place");
		final Uri uri = Uri.parse("android.resource://com.example/" + R.drawable.action_eating); 
		
		submitBtn = (Button) findViewById(R.id.reviewsubmitbutton);
		ratebar = (RatingBar) findViewById(R.id.rateratingbar);
		Session session = Session.getActiveSession();
		
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle params = new Bundle();
				
				params.putString("name", "I just made a new review!");
				params.putString("caption", "This place just earned a "+ratebar.getRating() + " rating!");
				params.putString("description", "I just made a new review for "+place.result.name+" !");
				params.putString("picture", "http://www.friendsmash.com/images/logo_large.jpg");
				//params.putParcelable("source", image);
				params.putString("picture",place.result.icon);

				showDialogWithoutNotificationBar("feed", params);
				
			}
		});
		makeMeRequest(session);
		
	}

	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    Log.d("Review",user.getName());
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	private void showDialogWithoutNotificationBar(String action, Bundle params){
		dialog = new WebDialog.Builder(this, Session.getActiveSession(), action, params).
			    setOnCompleteListener(new WebDialog.OnCompleteListener() {
			    @Override
			    public void onComplete(Bundle values, FacebookException error) {
			        if (error != null && !(error instanceof FacebookOperationCanceledException)) {
			            error.printStackTrace();
			        }
			        dialog = null;
			        dialogAction = null;
			        dialogParams = null;
			    }
			}).build();

			Window dialog_window = dialog.getWindow();
			dialog_window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);

			dialogAction = action;
			dialogParams = params;

			dialog.show();
	}

}
