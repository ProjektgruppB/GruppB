package com.gruppb.maptest2;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class LegalNoticesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.legal_notices);
		
		((TextView) findViewById(R.id.textId)).setText(GooglePlayServicesUtil
				.getOpenSourceSoftwareLicenseInfo(this));
	}

}
