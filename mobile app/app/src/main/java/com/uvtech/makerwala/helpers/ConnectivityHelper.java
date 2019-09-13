package com.uvtech.makerwala.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.uvtech.makerwala.ApplicationLoader;

public class ConnectivityHelper {

    public static boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationLoader.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
