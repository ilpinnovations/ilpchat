package com.tcs.tvmilp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
//this class is responsible for removing the groups from the "L-G" cloumn of the user table in the parse database.
public class ConfirmRemoveGroup extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.confirmremovegroup);
		Button yes=(Button)findViewById(R.id.buttonconfirmdeleteyes);
		Button no=(Button)findViewById(R.id.buttonconfirmdeleteno);
		yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String name=null;
				String names=null;
				CreateGroup cg=new CreateGroup();
				Bundle extras=getIntent().getExtras();
				if(extras!=null)
				{
					name=extras.getString("name");
					names=ParseUser.getCurrentUser().getString("L_G");
					System.out.println("the names reveived from the intent are "+names);
				}
				//String groupnames=cg.fetchGroups();
				//cg.deleteItem(num,names);
				//cg.b.performClick();
				Intent i0=new Intent(getApplicationContext(),CreateGroup.class);
				i0.putExtra("action","yes");
				i0.putExtra("itemname",name);
				i0.putExtra("returnedNames", names);
				startActivity(i0);
				
				
			}
		});
		no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i1=new Intent(getApplicationContext(),CreateGroup.class);
				i1.putExtra("action","no");
				startActivity(i1);
			}
		});
	}
	

	
	
}
