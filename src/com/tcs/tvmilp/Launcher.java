package com.tcs.tvmilp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Launcher extends Activity {
	String previousRole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// the below code will check if the user permission in the role table is
		// same as in the user table. In case of mismatch
		// below code will change the entry in the user table.

		// this code is to check if the user is already logged in. If he is
		// logged in then the below code will take user
		// directly to the activity and he does not have to log in angain.
		Bundle extras=getIntent().getExtras();
		if(extras!=null)
		{
			int code=extras.getInt("exit");
			if(code==0)
			{
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				super.onDestroy();
				System.exit(0);
			}
		}
		
		
		Intent check;
		if (ParseUser.getCurrentUser() == null) {
			check = new Intent(getApplicationContext(), RegisterActivity.class);
			startActivity(check);
		} else {

			// the below code will check if the user permission in the role
			// table is same as in the user table. In case of mismatch
			// below code will change the entry in the user table.
			previousRole = ParseUser.getCurrentUser().getString("role");
			System.out.println("the current role tagged to the usert is"
					+ previousRole);
			String userName = ParseUser.getCurrentUser().getUsername();
			System.out.println("The name of the user is" + userName);
			ParseQuery<ParseObject> roleQuery = ParseQuery
					.getQuery("roletable");
			roleQuery.whereEqualTo("username", userName);
			roleQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					// TODO Auto-generated method stub
					if (arg1 == null) {
						ParseObject po1 = null;
						try {
							po1 = arg0.get(0);
							System.out
									.println("the role in the role table of user is"
											+ po1.getString("role").toString());
							System.out
									.println("the previous role assigned to the user is"
											+ previousRole);
							String comp1=po1.getString("role").toString();
							String comp2=previousRole;
							if(!comp1.equals(comp2))
							{
								ParseUser pu=ParseUser.getCurrentUser();
								pu.put("role", comp1);
								pu.saveInBackground();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						arg1.printStackTrace();
					}
				}
			});

			// code for taking to different activities starts from here.
			String urole = ParseUser.getCurrentUser().getString("role");
			System.out.println("the role of the associate is" + urole);
			if (urole.equals("superadmin")) {
				System.out.println("going to super admin intent");
				Intent superAdmin = new Intent(getApplicationContext(),
						SuperAGrant.class);
				startActivity(superAdmin);
			}
			if (urole.equals("trainee")) {

				if (ParseUser.getCurrentUser().getString("L_G") != null) {
					System.out.println("lgname is null for this user");
					Intent chatIntent = new Intent(getApplicationContext(),
							ChatPage.class);
					startActivity(chatIntent);

				} else {
					System.out.println("lgname is  null for this user");
					Intent selectGroup = new Intent(getApplicationContext(),
							SelectGroup.class);
					startActivity(selectGroup);
				}
			}

			if (urole.equals("admin")) {
				Intent createGroup = new Intent(getApplicationContext(),
						CreateGroup.class);
				startActivity(createGroup);
			}

		}
	}

}
