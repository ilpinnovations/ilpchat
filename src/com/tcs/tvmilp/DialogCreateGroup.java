package com.tcs.tvmilp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//this is the dialog activity in which user enters the name of the group to be created.
public class DialogCreateGroup extends Activity{
	EditText groupName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dialogcreategroup);
		groupName=(EditText)findViewById(R.id.dialogeditetextcreategroup);
		Button b=(Button)findViewById(R.id.dialogbuttoncreategroup);
		//now creating listener for the button created in the dialog box.
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				createGroup();
				Intent i=new Intent(getApplicationContext(),CreateGroup.class);
				startActivity(i);
			}
		});
		
		
	}
	//this is the method to create a new learning group
	
	public void createGroup()
	{
		//this is the method to add the entries in the "L-G" User table of the parse cloud database.
		final String uGroupName =groupName.getText().toString();
		// this is the code to add a new table for a group in which all
		// the chat messages will be stored.
		System.out.println("The name of the table is" + uGroupName);
		ParseObject po = new ParseObject(uGroupName);
		po.put("text", "Hello"
				+ ParseUser.getCurrentUser().getUsername());
		po.put("username", ParseUser.getCurrentUser().getString("name"));
		
		po.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException exception) {
				// TODO Auto-generated method stub
				if (exception == null)
					System.out.println("values inserted successfully");

			}
		});
		// this code will create a table in which contains list of all
		// the tables
		ParseObject po1 = new ParseObject("GroupTable");
		po1.put("name", uGroupName);
		po1.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException exception) {
				// TODO Auto-generated method stub

				if (exception == null)
					System.out
							.println("group table created successfully");

			}
		});
		// this code will add admin to that group i.e. update the L_G
		// column of the User Table
		// This code will update the column value of l-g in
		ParseUser
				.getQuery()
				.whereEqualTo("username",
						ParseUser.getCurrentUser().getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> update,
							ParseException arg1) {
						// TODO Auto-generated method stub
						ParseObject po = update.get(0);
						String previouslg = po.getString("L_G");
						if(previouslg==null)
						{
							po.put("L_G", uGroupName);
						}
						else
						{
						po.put("L_G", previouslg + "," + uGroupName);
						}
						po.saveInBackground();
						
					}
				});
	}

	
	
}
