package com.tcs.tvmilp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
//this is the login activity. No user will see this activity. The user will be logged in from this activity.
public class Login extends Activity {

	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.login);

		// final EditText empid = (EditText) findViewById(R.id.logempid);
		// final EditText password = (EditText) findViewById(R.id.logpasswd);
		Button login = (Button) findViewById(R.id.loginbutton);
		// Button register = (Button) findViewById(R.id.logregbtn);
		System.out.println("Now inside the login activity");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			username = extras.getString("username");
		}
		final ParseUser pu = new ParseUser();
		Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

		Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
		try {
			onReceive(this, i);
		} catch (Exception e) {
			System.out.println(e);
		}

		// String username = ParseUser.getCurrentUser().getString("username");

		String pass = "password";
		System.out.print(username);
		System.out.println(pass);
		final ProgressDialog dia = ProgressDialog.show(Login.this, null,
				"Loading Please wait...");

		ParseUser.logInInBackground(username, pass, new LogInCallback() {

			@Override
			public void done(ParseUser arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg0 != null) {
					System.out.print("Login Successful");
					
					dia.dismiss();

					String urole = ParseUser.getCurrentUser().getString("role");
					System.out.println("the role of the associate is" + urole);
					if (urole.equals("trainee")) {
						Intent selectGroup = new Intent(
								getApplicationContext(), SelectGroup.class);
						startActivity(selectGroup);
					}

					if (urole.equals("admin")) {
						Intent createGroup = new Intent(
								getApplicationContext(), CreateGroup.class);
						startActivity(createGroup);
					}
					if (urole.equals("superadmin")) {
						System.out.println("going to super admin intent");
						Intent superAdmin = new Intent(getApplicationContext(),
								SuperLogin.class);
						startActivity(superAdmin);
					}

					// this will take you to chat page directly
					/*
					 * Intent i = new Intent( "android.intent.action.CHATPAGE");
					 * startActivity(i);
					 */

				} else {
					System.out.print("Login Unsuccessful");
					Toast.makeText(getApplicationContext(), "Login failed",
							Toast.LENGTH_LONG).show();
					System.out.println(arg1);
				}

			}
		});

	}

	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String jsonData = extras.getString("com.parse.Data");
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonData);
			String heading = jsonObject.getString("heading");
			String dataString = jsonObject.getString("dataString");
			System.out.println("push notification is1" + dataString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
