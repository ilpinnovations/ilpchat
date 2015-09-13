package com.tcs.tvmilp;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
//this activity is for the password feature for the superadmin.
public class SuperLogin extends Activity {

	String empid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		final EditText password = (EditText) findViewById(R.id.logpasswd);
		
		Button login = (Button) findViewById(R.id.loginbutton);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			empid = extras.getString("username");
			System.out.println("the value received from the register is"
					+ empid);
		}
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ParseUser.logInInBackground(empid, password.getText()
						.toString(), new LogInCallback() {

					@Override
					public void done(ParseUser arg0, ParseException arg1) {
						// TODO Auto-generated method stub
						if(arg1==null)
						{
						Intent i = new Intent(getApplicationContext(),
								SuperAGrant.class);
						startActivity(i);
						}
						else
						{
							Toast.makeText(getApplicationContext(),
									"Invalid Password", Toast.LENGTH_LONG).show();
						}
						
					}
				});

			}
		});

	}

}
