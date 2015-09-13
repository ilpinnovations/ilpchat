package com.tcs.tvmilp;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//this is the userdetails page that will display the user details fetched from the databse
public class UserDetails extends Activity {

	String username;
	ProgressDialog dia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetails);
		
		MyTask task=new MyTask();
		dia = ProgressDialog.show(UserDetails.this, null,
				"Loading Please wait...");
		task.execute();
		dia.dismiss();
		
       
	}

	private class MyTask extends AsyncTask<Void, Void, Void>{

		
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			
			final TextView name=(TextView)findViewById(R.id.detname);
			final TextView email=(TextView)findViewById(R.id.detmail);
			final TextView hostel=(TextView)findViewById(R.id.dethostel);
			final TextView phone=(TextView)findViewById(R.id.detphone);
			final TextView lg=(TextView)findViewById(R.id.detlg);
			final TextView role=(TextView)findViewById(R.id.detaccess);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				username = extras.getString("username");
				System.out.println("the username on the other page is" + username);

			}

			ParseQuery<ParseObject> query = ParseQuery.getQuery("questionstmf55");
	        //query.whereEqualTo("username", username);
			ParseUser.getQuery().whereEqualTo("username", username).findInBackground(new FindCallback<ParseUser>() {
				
				@Override
				public void done(List<ParseUser> arg0, ParseException exception) {
					// TODO Auto-generated method stub
					if(exception==null)
					{
						ParseObject po=arg0.get(0);
						String urole=ParseUser.getCurrentUser().getString("role");
						System.out.println("the role of the person is"+urole);
						
						String uemail=po.getString("email");
						String uhostel=po.getString("hostel");
						String uphone=po.getString("phone");
						String ulg=po.getString("L_G");
						String uname=po.getString("name");
					    name.setText(uname);
					    email.setText(uemail);
					    hostel.setText(uhostel);
					    phone.setText(uphone);
					    lg.setText(ulg);
						
						
					}
				}
			});
			
			
			return null;
		}
		
	}
	
}
