package com.tcs.tvmilp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;
// this activity registers the application on the parse cloud.
public class Ilpapplication extends Application{

	
	public void onCreate()
	{
		
		super.onCreate();
		System.out.println("coming to register class successfully");
		ParseObject.registerSubclass(ParseRegister.class);
		System.out.println("coming to initialization class successfully");
		Parse.initialize(this, "<Parse Applicaiton Id>",
				"<Parse Client Key>");
		 //PushService.setDefaultPushCallback(this, PushReceiver.class); 
	}
}
