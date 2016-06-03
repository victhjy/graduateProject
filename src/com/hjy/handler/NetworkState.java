package com.hjy.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState {
	public static boolean isNetworkAvailable(Context context) {   
	    ConnectivityManager cm = (ConnectivityManager) context   
	            .getSystemService(Context.CONNECTIVITY_SERVICE);   
	    if (cm == null) {   
	    } else {
/*�?//如果仅仅是用来判断网络连�?
	�?�?  //则可以使�? cm.getActiveNetworkInfo().isAvailable();  
*/	        
	    	NetworkInfo[] info = cm.getAllNetworkInfo();   
	        if (info != null) {   
	            for (int i = 0; i < info.length; i++) {   
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
	                    return true;   
	                }   
	            }   
	        }   
	    }   
	    return false;   
	} 

}

