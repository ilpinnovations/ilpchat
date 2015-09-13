package com.tcs.tvmilp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SuperAGrant extends Activity {
//this is the base page for the superadmin. 
	ArrayList<ParseUser> assoclist;
	ListView listview;
	String employeeId,action;
	EditText searchAdmin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supermakeadmin);
		searchAdmin=(EditText)findViewById(R.id.superadminsearchedittext);
		Button makeadmin=(Button)findViewById(R.id.buttonsupermakeadmin);
		Button grouplist=(Button)findViewById(R.id.buttonsupergrouplist);
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null)
		{
		System.out.println("the values of bundle is not null");
		action=extras.getString("superaction");
		employeeId=extras.getString("empid");
		System.out.println("the value of received intent empid is"+employeeId);
		try
		{
		if(action.equals("yes"))
		{
			makeMeAdmin(employeeId);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		}
		makeadmin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				employeeId=searchAdmin.getText().toString();
				Intent goIntent=new Intent(getApplicationContext(),ConfirmMakeAdmin.class);
				goIntent.putExtra("username", employeeId);
				startActivity(goIntent);
				//makeMeAdmin();
				
			}
		});
		grouplist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				Intent gplist=new Intent(getApplicationContext(),SelectGroup.class);
				startActivity(gplist);
				
			}
		});
		
		

		
	}
	//this admin will set the entries in the "roletable" on the parse cloud database.
	public void makeMeAdmin(String eid)
	{
		
		String innerId=eid;
		
		//employeeId=searchAdmin.getText().toString();
		//final String tempName=empid;
		System.out.println("the entered value of employee id is"+innerId);
		ParseQuery<ParseObject> roleQuery=ParseQuery.getQuery("roletable");
		roleQuery.whereEqualTo("username",innerId);
		roleQuery.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				
				if(arg1==null)
				{
					ParseObject po1=arg0.get(0);
					System.out.println("the role of main main is"+po1.getString("role"));
					po1.put("role","admin");
					po1.saveInBackground();
				}
				else
				{
					arg1.printStackTrace();
				}
				
			}
		});
		
		
		
		
		
		
	}
	boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		
		
			if (doubleBackToExitPressedOnce) {
		       
		       
				super.onBackPressed();
				

					
					Intent i=new Intent(getApplicationContext(),Launcher.class);
					i.putExtra("exit", 0);
					startActivity(i);
					//android.os.Process.killProcess(android.os.Process.myPid());
					//super.finish();
					//Intent intent = new Intent(Intent.ACTION_MAIN);
					//intent.addCategory(Intent.CATEGORY_HOME);
					//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//startActivity(intent);
					//finish();
					// android.os.Process.killProcess(android.os.Process.myPid());
				}
			
			 Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
		        
		        this.doubleBackToExitPressedOnce = true;
		 	   

			    new Handler().postDelayed(new Runnable() {

			        @Override
			        public void run() {
			            doubleBackToExitPressedOnce=false;                       
			        }
			    }, 2000);
			    return;
		    
	}

}