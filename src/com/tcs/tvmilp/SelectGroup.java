package com.tcs.tvmilp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SelectGroup extends Activity {

	ArrayList<ParseObject> assoclist;
	ListView listview;
	//this is the select grouppage. The list of all the users are displayed here.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selectgroup);
		listview = (ListView) findViewById(R.id.selctgrouplist);
		listview.setScrollContainer(false);
		listview.setClickable(false);
		fetchPending();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				System.out.println("the item clicked is" + position);
				QueryAdapter a = new QueryAdapter();
				ParseObject u = a.getItem(position);
				String lgname = u.getString("name");
				String role = ParseUser.getCurrentUser().getString("role");
				System.out.println("Group name is clicked" + lgname);

				Intent i = new Intent(getApplicationContext(), ChatPage.class);
				i.putExtra("lgname", lgname);
				startActivity(i);

			}
		});

	}

	public void fetchPending() {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupTable");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub

				assoclist = new ArrayList<ParseObject>(arg0);
				listview.isSmoothScrollbarEnabled();
				listview.setAdapter(new QueryAdapter());

			}
		});

	}
	//this is the addGroup page. This method will save the entry of the "L-G" in the UserTable on parse cloud database.
	public void addGroup(String lgname) {

		final String lg_name = lgname;
		ParseUser
				.getCurrentUser()
				.getQuery()
				.whereEqualTo("username",
						ParseUser.getCurrentUser().getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> arg0, ParseException arg1) {
						// TODO Auto-generated method stub

						ParseObject po11 = arg0.get(0);
						po11.put("L_G", lg_name);
						po11.saveInBackground();

					}
				});

		Intent i = new Intent(getApplicationContext(), ChatPage.class);
		startActivity(i);
	}

	/*
	 * public void saveLG(String lgname) { String tempName = lgname; ParseUser
	 * pu = ParseUser.getCurrentUser(); pu.put("L_G", tempName);
	 * pu.saveEventually(new SaveCallback() {
	 * 
	 * @Override public void done(ParseException arg0) { // TODO Auto-generated
	 * method stub
	 * 
	 * } }); }
	 */

	private class QueryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return assoclist.size();
		}

		@Override
		public ParseObject getItem(int arg0) {
			// TODO Auto-generated method stub
			return assoclist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		// this is the base adapter method that will inflate the view.
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final int i = arg0;
			// TextView tv=(TextView)findViewById(R.id.sendbubble);
			// tv.setText("This is list view");
			if (arg1 == null) {

				arg1 = getLayoutInflater()
						.inflate(R.layout.nomallistitem, null);
				System.out.println("inflator is working fine");
			}
			// Position of the Associate in the list retrieved from the
			// database.
			// ParseObject user=getItem(arg0);
			// TextView tv = new TextView(getApplicationContext());
			// tv.setText("hello this is dynamically added");

			TextView username = (TextView) arg1;
			System.out.println("the value of i is" + i);

			// username.addView(b, 0,LayoutParams)

			// username.addView(tv, 0);
			final ParseObject ponew = assoclist.get(arg0);
			username.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View viw) {
					// TODO Auto-generated method stub
					System.out.println("tv is clicked" + i);

					// this method will tag a person to a group.
					addGroup(ponew.getString("name").toString());

				}
			});
			username.inflate(getApplicationContext(), R.layout.chatsent, null);

			username.setText(ponew.get("name").toString());
			username.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ParseUser pu=ParseUser.getCurrentUser();
					final String role=pu.getString("role");
					// TODO Auto-generated method stub
					final TextView innerTv = (TextView) arg0;
					// System.out.println("text view pressed is " + number);

					System.out.println("converting to text view"
							+ innerTv.getText().toString().trim());
					ParseUser
							.getCurrentUser()
							.getQuery()
							.whereEqualTo("username",
									ParseUser.getCurrentUser().getUsername())
							.findInBackground(new FindCallback<ParseUser>() {

								@Override
								public void done(List<ParseUser> arg0,
										ParseException arg1) {
									// TODO Auto-generated method stub

									Intent toConfirmSelectGroup = new Intent(
											getApplicationContext(),
											ConfirmSelectGroup.class);
									toConfirmSelectGroup
											.putExtra("tablename", innerTv
													.getText().toString()
													.trim());
									
									
									startActivity(toConfirmSelectGroup);
									

								}
							});

				}
			});
			System.out.println("TextView is working fine");
			// now changing the name of each field by modifying the textview.

			return arg1;

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ParseUser pu=ParseUser.getCurrentUser();
		String role=pu.getString("role");
		if(role.equals("superadmin"))
		{
			Intent i=new Intent(getApplicationContext(),SuperAGrant.class);
			startActivity(i);
		}
		
		
	}
	
}
