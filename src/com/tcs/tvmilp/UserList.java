package com.tcs.tvmilp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//this page will display the list of the users present in teh lg.
public class UserList extends Activity{

	
	public ArrayList<ParseUser> assoclist;
	
	public static ParseUser user;
	ListView listview;
	String tableName;
	InternetChecker internetChecker;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null)
		{
			tableName=extras.getString("tablename");
			System.out.println("the table name received in the userlist is"+tableName);
		}
		
		listview=(ListView)findViewById(R.id.list);
		//This is the listerner on the list of the andorid application that will do the work.
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				System.out.println("the item clicked is"+position);
				AssociateAdapter a=new AssociateAdapter();
				ParseUser u=a.getItem(position);
				System.out.println("the userr name is"+u.getUsername());
				String user=u.getUsername();
				String role=ParseUser.getCurrentUser().getString("role");
				if(role.equals("admin")||role.equals("superadmin"))
				{
				Intent i=new Intent(getApplicationContext(),UserDetails.class);
				i.putExtra("username", u.getUsername());
				startActivity(i);
				}
				
				
			}
		});
		
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			
		
		loadAssociateList();
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
		internetChecker=new InternetChecker(getApplicationContext());
		if(!internetChecker.isConnectingToInternet())
		{
			System.out.println("INTERNET CRASHED");
			Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
		}
		
	}


	public void loadAssociateList() throws ParseException
	{
		
		ParseObject po=new ParseObject("Register");
	   
	
		final ProgressDialog dia = ProgressDialog.show(this, null,
				"Loading Please wait...");
		internetChecker=new InternetChecker(getApplicationContext());
		if(!internetChecker.isConnectingToInternet())
		{
			dia.dismiss();
		}
			ParseUser pu=ParseUser.getCurrentUser();
			
		ParseUser.getQuery().whereEqualTo("L_G",tableName).findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> list, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg1!=null)
				{
					dia.dismiss();
					//Toast.makeText(getApplicationContext(), arg1.toString(), Toast.LENGTH_LONG).show();
					
				}
				if(list==null)
				{
					//Toast.makeText(getApplicationContext(), "No user in database", Toast.LENGTH_LONG).show();
				}
				else
				{
					dia.dismiss();
					assoclist =new ArrayList<ParseUser>(list);
					
					
					listview.setAdapter(new AssociateAdapter());
					
					
					
					
				}
				
			}
		});
	}
	
	
   class AssociateAdapter extends BaseAdapter
{

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return assoclist.size();
	}

	@Override
	public ParseUser getItem(int arg0) {
		// TODO Auto-generated method stub
		return assoclist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1==null)
		{
			arg1=getLayoutInflater().inflate(R.layout.listitem,null);
			System.out.println("inflator is working fine");
		}
		//Position of the Associate in teh list retrieved from the database.
		ParseUser user=getItem(arg0);
		TextView username=(TextView) arg1;
		System.out.println("TextView is working fine");
		//now changing the name of each field by modifying the textview.

		username.setText(user.getString("name"));
		
		
		return arg1;
	}
	
}


@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	ParseUser pu=ParseUser.getCurrentUser();
	String role=pu.getString("role");
	if(role.equals("admin"))
	{
	Intent i=new Intent(getApplicationContext(),CreateGroup.class);
	startActivity(i);
	}
	
}
   
	
}

