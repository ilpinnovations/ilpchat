package com.tcs.tvmilp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
//this is the dialog activity that will ask to confirm the admin making prcess. This page is only visible to the super admin.
public class ConfirmMakeAdmin extends Activity {
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.confirmmakeadmin);
		TextView tv = (TextView) findViewById(R.id.textvwsuperconfirm);
		Button yes = (Button) findViewById(R.id.buttonsuperyes);
		Button no = (Button) findViewById(R.id.buttonsuperno);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
		}
		tv.setText(username);
		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent(getApplicationContext(),
						SuperAGrant.class);
				returnIntent.putExtra("superaction", "yes");
				returnIntent.putExtra("empid", username);
				startActivity(returnIntent);
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent(getApplicationContext(),
						SuperAGrant.class);
				returnIntent.putExtra("superaction", "no");
				startActivity(returnIntent);
			}
		});

	}

}
