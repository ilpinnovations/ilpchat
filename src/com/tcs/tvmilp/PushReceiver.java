package com.tcs.tvmilp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

public class PushReceiver extends ParsePushBroadcastReceiver {

	//this is the push revceiver. All the pushes are received here.
	boolean b;
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		//sharedPreferences=getSharedPreferences();
		PushChecker pushchecker=new PushChecker(context);
		pushchecker.updateData("true");
		String data=pushchecker.readData();
		System.out.println("the data in the table after the update push push"+data);
		System.out.println("yeah!!! push is received");
		b=true;
		ParseUser pu=ParseUser.getCurrentUser();
		String role=pu.getString("role");
		//this if condition is because superadmin should not receive push notification.
		if(!role.equals("superadmin"))
		{
		JSONObject json=null;
		String pushData=null;
		String name=arg1.getExtras().getString(KEY_PUSH_CHANNEL);
		try {
			json = new JSONObject(arg1.getExtras().getString(KEY_PUSH_DATA));
			pushData=json.getString("alert");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*if(json!=null) {
		    Intent pushIntent = new Intent();
		    pushIntent.setClassName(context, "com.tcs.tvmilp.ChatPage");
		    pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    pushIntent.putExtra("store", pushData);
		    context.startActivity(pushIntent);
		}*/  
		NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
        Notification notify=new Notification(R.drawable.ic_launcher,name,System.currentTimeMillis());
        notify.defaults |=Notification.DEFAULT_VIBRATE;
        notify.defaults |=Notification.DEFAULT_LIGHTS;
        notify.defaults |=Notification.DEFAULT_SOUND;
        Intent i=null;
        if(role.equals("admin"))
        {
        	i=new Intent(context,CreateGroup.class);
        }
        else
        {
        	i=new Intent(context,ChatPage.class);
        }
        PendingIntent pending= PendingIntent.getActivity(context, 0,i, 0);
        
        notify.setLatestEventInfo(context,name,pushData,pending);
        notif.notify(0, notify);
		}

	}
	
	

	public boolean pushChecker()
	{
		return b;
	}

	@Override
	protected void onPushOpen(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		super.onPushOpen(arg0, arg1);
		for(int i=0;i<10;i++)
		System.out.println("yeah!!! push is opened");
	}

	@Override
	protected void onPushReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		super.onPushReceive(arg0, arg1);
		for(int i=0;i<10;i++)
		System.out.println("yeah!!! push is opened123");
	}
	
	
	
}
