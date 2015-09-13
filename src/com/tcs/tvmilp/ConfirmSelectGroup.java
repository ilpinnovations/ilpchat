package com.tcs.tvmilp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
//this class is responsible for confirming the group selection feature by the trainee.
public class ConfirmSelectGroup extends Activity{
	String lgname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.confirmselectpage);
		TextView tv=(TextView)findViewById(R.id.confirmselectlg);
		Button yes=(Button)findViewById(R.id.buttonconfirmselectyes);
		Button no=(Button)findViewById(R.id.buttonconfirmselectno);
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null)
		{
			lgname=extras.getString("tablename");
			
		}
		
		tv.setText(lgname);
		yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ParseUser pu=ParseUser.getCurrentUser();
				pu.put("L_G", lgname);
				pu.saveInBackground();
				Intent chatIntent = new Intent(getApplicationContext(),
						ChatPage.class);
				chatIntent.putExtra("tablename", lgname);
				startActivity(chatIntent);
				
				
			}
		});
		no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent goBack=new Intent(getApplicationContext(),SelectGroup.class);
				startActivity(goBack);
			}
		});
	}

}
