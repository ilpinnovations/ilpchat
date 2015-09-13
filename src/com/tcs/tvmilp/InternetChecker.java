package com.tcs.tvmilp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//this activity checks if the internet connection is present on the device.
public class InternetChecker {

	private Context context;
	  
    public InternetChecker(Context context){
        this.context = context;
    }
  
   
	//this method checks for internet connection
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
  
          }
          return false;
    }

}
