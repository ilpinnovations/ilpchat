package com.tcs.tvmilp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//this is the class for deleting the images videos and text from the database.
public class DeleteDialog extends Activity{

	String tableName;
	String objectId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.delete);
		Button yes=(Button)findViewById(R.id.buttondeleteyes);
		Button no=(Button)findViewById(R.id.buttondeleteno);
		Bundle extras=getIntent().getExtras();
		if(extras!=null)
		{
			tableName=extras.getString("tablename");
			objectId=extras.getString("objectid");
			System.out.println("the table name received by the deleter is"+tableName);
			System.out.println("the object id received by teh deleter is "+objectId);
		}
		ParseUser pu=ParseUser.getCurrentUser();
		final String role=pu.getString("role");
		yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ParseQuery<ParseObject> query=ParseQuery.getQuery(tableName);
				//ParseObject deleter=new ParseObject(tableName);
				System.out.println("the object id in the delete is"+objectId);
				query.whereEqualTo("objectId",objectId);
				query.findInBackground(new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> arg0, ParseException arg1) {
						// TODO Auto-generated method stub
						
						
						try {
							ParseObject po=arg0.get(0);
							
							po.delete();
							System.out.println("DELETE DELETE DELETE");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				//deleter.put("objectId", objectId);
				
				Intent goBack=new Intent(getApplicationContext(),ChatPage.class);
				goBack.putExtra("tablename", tableName);
				startActivity(goBack);
			}
		});
		
		
		no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goBack=new Intent(getApplicationContext(),ChatPage.class);
				goBack.putExtra("tablename", tableName);
				startActivity(goBack);
			}
		});
		
		
	}

	
	
}
