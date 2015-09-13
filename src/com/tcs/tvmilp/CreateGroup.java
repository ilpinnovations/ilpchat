package com.tcs.tvmilp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CreateGroup extends Activity {
	//this is the class for creating the groups and making the entries in the "L-G" column of the User table in the parse cloud database.
	ListView listview;
	ArrayList<String> assoclist;
	String groupnames;
	boolean isConfirmed = false;
	int number = 0;
	Button b;
	String action;
	String name;
	String newValue;
	int counter;
	TextView tv;
	String mainString;
	private Toolbar mToolbar;
	Handler handler;
	Runnable r;
	boolean isRunning;
	QueryAdapter qa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.creategroup);
		
		System.out.println("the name of the first user is"
				+ ParseUser.getCurrentUser().getUsername());
		listview = (ListView) findViewById(R.id.creategrouplist);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		handler = new Handler();
		qa = new QueryAdapter();
		listview.setItemsCanFocus(false);
		try {

			fetchGroups();
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				action = extras.getString("action");
				name = extras.getString("itemname");
				groupnames = extras.getString("returnedNames");

				if (action.equals("yes")) {
					isConfirmed = true;
					System.out.println("the value of confirmed is now "
							+ isConfirmed);
					System.out
							.println("the name in the list received by the system is"
									+ name);
				}

			}
		} catch (Exception e) {
			System.out.println(e);
		}

		listview.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		Button createGroup = (Button) findViewById(R.id.buttoncreategroup);

		// listview.setOnItemClickedListene
		// clicking this button will create a new group.

		createGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(),
						DialogCreateGroup.class);
				startActivity(i);

			}

		});
		fetchGroups();
		if (isConfirmed) {

			if (groupnames != null)

				deleteItem(name);
			isConfirmed = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("RESUME ACTIVATED");

		// ParseObject po1 = arg0.get(0);
		// mainString = po1.getString("L_G");

		if (isConfirmed) {
			// deleteItem(position);
			isConfirmed = false;

		}

		fetchGroups();
		
		//listview.setAdapter(qa);
		//qa.notifyDataSetChanged();

	}

	public String fetchGroups() {
		// this code fetches the data in the background thread
		ParseUser
				.getQuery()
				.whereEqualTo("username",
						ParseUser.getCurrentUser().getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> arg0, ParseException arg1) {
						// TODO Auto-generated method stub
						assoclist = new ArrayList<String>();
						for (int i = 0; i < arg0.size(); i++) {
							groupnames = arg0.get(i).getString("L_G");
							if (!(arg0.get(i).getString("L_G") == null)) {
								String[] names = groupnames.split(",");
								for (int j = 0; j < names.length; j++) {
									if (names[j] != null)
										assoclist.add(names[j]);

								}
							}
						}
						listview.setAdapter(qa);
						qa.notifyDataSetChanged();

						// listview.setOnItemClickListener(new View.)

					}
				});
		// this codes fetches the data on main thread
		/*
		 * try { List<ParseUser>
		 * list=ParseUser.getQuery().whereEqualTo("username",
		 * ParseUser.getCurrentUser().getUsername()).find(); for (int i = 0; i <
		 * list.size(); i++) { groupnames = list.get(i).getString("L_G");
		 * System.out.println("the final 123 final is "+groupnames); String[]
		 * names = groupnames.split(","); assoclist = new ArrayList<String>();
		 * for (int j = 0; j < names.length; j++) { assoclist.add(names[j]); } }
		 * listview.setAdapter(new QueryAdapter());
		 * 
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		return groupnames;
	}

	public void deleteItem(String name1) {
		final String nameInside = name1;
		System.out.println("the name to be deleted is" + nameInside);

		// Bundle extras = getIntent().getExtras();
		// final int i = extras.getInt("itemposition");
		System.out.println("the name of the user is");

		System.out.println("the value received is4321ee" + nameInside);

		String str1 = ParseUser.getCurrentUser().getString("L_G");
		try {
			List<ParseUser> po = ParseUser
					.getCurrentUser()
					.getQuery()
					.whereEqualTo("username",
							ParseUser.getCurrentUser().getUsername()).find();
			ParseUser po1 = po.get(0);
			mainString = po1.getString("L_G");
			System.out.println("the username for first user is" + mainString);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("the value of the names string is" + mainString);

		ParseUser.getQuery().whereEqualTo("L_G", mainString)
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> result, ParseException arg1) {
						// TODO Auto-generated method stub
						ParseObject po = result.get(0);
						String previousData = po.getString("L_G");
						System.out.println("the main main main data is "
								+ previousData);
						String[] tempData = previousData.split(",");
						String temp1;
						String finalTemp = null;
						for (int k = 0; k < tempData.length; k++) {
							temp1 = tempData[k];
							if (!nameInside.equals(temp1)) {
								if (finalTemp == null) {
									finalTemp = temp1;
								} else {
									finalTemp = finalTemp + "," + temp1;
								}
							}

						}

						if (finalTemp == null) {

							System.out.println("null executed");
							po.put("L_G", JSONObject.NULL);
						} else {
							po.put("L_G", finalTemp);
						}

						System.out
								.println("The updated data in the database is"
										+ finalTemp);
						po.saveInBackground();

					}
				});
		fetchGroups();
		// qa.notifyDataSetChanged();
		Intent restart = new Intent(getApplicationContext(), CreateGroup.class);
		startActivity(restart);
		isConfirmed = false;

	}

	private class QueryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return assoclist.size();
		}

		@Override
		public String getItem(int arg0) {
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
			number = arg0;
			System.out.println("the value of the previous argument is" + arg0);
			// TextView tv=(TextView)findViewById(R.id.sendbubble);
			// tv.setText("This is list view");
			// ParseUser user = getItem(arg0);
			String name = getItem(arg0);
			if (arg1 == null) {

				arg1 = getLayoutInflater()
						.inflate(R.layout.grouplistitem, null);
				System.out.println("inflator is working fine");

			}

			tv = new TextView(getApplicationContext());

			tv.setText("  " + name);

			b = new Button(getApplicationContext());

			b.setBackgroundResource(R.drawable.remove_button);
			LinearLayout username = (LinearLayout) arg1;

			tv.setHeight(60);
			tv.setTextSize(20);
			tv.setWidth(350);
			tv.setTextColor(Color.parseColor("#000000"));
			b.setText(name);
			// here the text in hidden in the button so that later on it can be
			// referred for deleting item.
			b.setTextColor(getResources().getColor(android.R.color.transparent));
			// b.setVisibility(View.GONE);
			b.setWidth(10);
			b.setHeight(10);
			username.addView(b, 0);
			username.addView(tv, 0);

			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					TextView innerTv = (TextView) arg0;
					System.out.println("text view pressed is " + number);

					System.out.println("converting to text view"
							+ innerTv.getText().toString().trim());
					Intent chatIntent = new Intent(getApplicationContext(),
							ChatPage.class);
					chatIntent.putExtra("tablename", innerTv.getText()
							.toString().trim());
					startActivity(chatIntent);
				}
			});

			b.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View viw) {
					// TODO Auto-generated method stub
					Button innerButton = (Button) viw;
					System.out.println("Button is clicked"
							+ innerButton.getText().toString().trim());

					Intent intent = new Intent(getApplicationContext(),
							ConfirmRemoveGroup.class);
					System.out.println("the experiment text is"
							+ tv.getText().toString() + "  " + number);

					intent.putExtra("name", innerButton.getText().toString()
							.trim());
					intent.putExtra("names", fetchGroups());
					startActivity(intent);
					System.out.println("button is is" + b.getId());
					// fetchGroups();
				}
			});

			return arg1;

		}

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
