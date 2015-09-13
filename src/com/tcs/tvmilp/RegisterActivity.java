package com.tcs.tvmilp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity

{

	Boolean checked = false;
	ArrayList<String> superUsers;
	boolean isSuperAdmin = false;
	boolean isExist = false;
	String existingUser;
	EditText name;
	EditText phone;
	EditText stream;
	EditText hostel;
	EditText empid;
	InternetChecker internetChecker;
	
	//this is the register activity that registers the users on the cloud database.
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);
		name = (EditText) findViewById(R.id.edname);
		phone = (EditText) findViewById(R.id.edphone);
		stream = (EditText) findViewById(R.id.edstream);
		hostel = (EditText) findViewById(R.id.edhostel);
		empid = (EditText) findViewById(R.id.edempid);
		superUsers = new ArrayList<String>();
		Button register = (Button) findViewById(R.id.regbutton);
		System.out.println("the query is working fine");
		
		/*
		 * ParseQuery<ParseObject> superQuery = ParseQuery
		 * 
		 * .getQuery("User"); superQuery.whereEqualTo("role", "superadmin");
		 * superQuery.findInBackground(new FindCallback<ParseObject>() {
		 * 
		 * @Override public void done(List<ParseObject> arg0, ParseException
		 * arg1) { // TODO Auto-generated method stub for (int i = 0; i <
		 * arg0.size(); i++) { ParseObject po = arg0.get(i);
		 * System.out.println("the super user name is" +
		 * po.getString("username"));
		 * 
		 * }
		 * 
		 * } });
		 */

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				MyTask task=new MyTask();
				task.execute();
			}
		});

	}
	
	
@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		internetChecker=new InternetChecker(getApplicationContext());
		if(!internetChecker.isConnectingToInternet())
		{
			System.out.println("INTERNET CRASHED");
			Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
		}
	}


private class MyTask extends AsyncTask<Void, Void, Void>
{
	ProgressDialog dia;
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dia.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dia = ProgressDialog.show(RegisterActivity.this, null,
				"Loading Please wait...");
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// this is the code to identify the super admin

		// this is the code to register a trainee
		String sname = name.getText().toString().trim();

		final String sphone = phone.getText().toString().trim();
		final String email = stream.getText().toString().trim();
		final String shostel = hostel.getText().toString().trim();
		final String sempid = empid.getText().toString().trim();

		

		List<ParseUser> userlist = new ArrayList<ParseUser>();
		

		try {
			userlist = ParseUser.getQuery().find();
			for (int i = 0; i < userlist.size(); i++) {
				ParseUser internalPo = userlist.get(i);
				String cUser = internalPo.getString("username");
				System.out.println("the username in the checker is"
						+ cUser);
				if (cUser.equals(sempid)) {
					System.out.println("user already exists");
					isExist = true;
					existingUser = internalPo.getString("username");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new FindCallback<ParseUser>() {

		/*
		 * @Override public void done(List<ParseUser> arg0,
		 * ParseException arg1) { // TODO Auto-generated method stub
		 * 
		 * for(int i=0;i<arg0.size();i++) { ParseObject
		 * internalPo=arg0.get(i); String
		 * cUser=internalPo.getString("username");
		 * System.out.println("the username in the checker is"+cUser);
		 * if(cUser.equals(sempid)) {
		 * System.out.println("user already exists"); isExist=true;
		 * existingUser=internalPo.getString("username"); }
		 * 
		 * }
		 * 
		 * } });
		 */
		if (!isExist) {
			System.out.println("entered non exist");
			ParseUser pu = new ParseUser();
			pu.setUsername(sempid);
			pu.setPassword("password");

			pu.put("name", sname);
			pu.put("hostel", shostel);
			pu.setEmail(email);
			pu.put("phone", sphone);
			pu.put("role", "trainee");

			pu.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(com.parse.ParseException arg0) {
					// TODO Auto-generated method stub
					if (arg0 == null) {
						Toast.makeText(getApplicationContext(),
								"Success", Toast.LENGTH_LONG).show();

						dia.dismiss();
						String urole = ParseUser.getCurrentUser()
								.getString("role");

						System.out
								.println("the role of the associate is"
										+ urole);

						Intent toLogin = new Intent(
								getApplicationContext(), Login.class);
						toLogin.putExtra("username", sempid);
						startActivity(toLogin);

					}
					if (arg0 != null) {
						Toast.makeText(getApplicationContext(),
								"	INVALID CREDENTIALS", Toast.LENGTH_SHORT)
								.show();
						System.out.println(arg0);
					}
					ParseObject po = new ParseObject("roletable");
					po.put("username", sempid);
					po.put("role", "trainee");
					po.saveInBackground(new SaveCallback() {

						@Override
						public void done(com.parse.ParseException arg0) {
							// TODO Auto-generated method stub

						}
					});
				}
			});

			// Initially this code will set everyone's role as trainee.
			if (!isSuperAdmin) {
				
			}

		} else {

			// This is the code to make a user who is already registered
			// log in again
			// I will compare the values and if the values are same then
			// I check all the values and
			// if the values are same then ,the user will log in.
			ParseUser.getCurrentUser().getQuery()
					.whereEqualTo("username", sempid)
					.findInBackground(new FindCallback<ParseUser>() {

						@Override
						public void done(List<ParseUser> arg0,
								com.parse.ParseException arg1) {
							// TODO Auto-generated method stub

							ParseUser innerpu = arg0.get(0);
							String inneremail = innerpu
									.getString("email");
							String hostel = innerpu.getString("hostel");
							String phone = innerpu.getString("phone");
							System.out.println("came to comparison step");
							if (inneremail.equals(email)
									&& hostel.equals(shostel)
									&& phone.equals(sphone)) {
								Intent log = new Intent(
										getApplicationContext(),
										Login.class);
								log.putExtra("username", sempid);
								startActivity(log);

							}

						}
					});

			// this is the code to identify the superadmin
			dia.dismiss();
			ParseUser.getQuery().whereEqualTo("role", "superadmin")
					.findInBackground(new FindCallback<ParseUser>() {

						@Override
						public void done(List<ParseUser> arg0,
								com.parse.ParseException arg1) {
							// TODO Auto-generated method stub

							for (int i = 0; i < arg0.size(); i++) {
								ParseObject po = arg0.get(i);
								System.out.println("the super user name is"
										+ po.getString("username"));
								String databaseUsername = po
										.getString("username");
								String enteredUsername = empid
										.getText().toString();
								if (databaseUsername
										.equals(enteredUsername)) {
									isSuperAdmin = true;
									System.out
											.println("User is superadmin and will be sent to login activity");
									Intent superIntent = new Intent(
											getApplicationContext(),
											SuperLogin.class);

									superIntent.putExtra("username",
											sempid);
									startActivity(superIntent);

								}

							}

						}
					});

			if (isSuperAdmin) {

			}
		}
		return null;
	}
	
}
}