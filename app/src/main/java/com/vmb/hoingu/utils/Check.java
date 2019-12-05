package com.vmb.hoingu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vmb.hoingu.R;

/**
 * Created by keban on 9/1/2017.
 */

public class Check {

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Utils.shortToast(context, context.getString(R.string.no_internet));
            return false;
        }

        if (!networkInfo.isConnected()) {
            Utils.shortToast(context, context.getString(R.string.no_internet));
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Utils.shortToast(context, context.getString(R.string.no_internet));
            return false;
        }

        if (!networkInfo.isConnectedOrConnecting()) {
            Utils.shortToast(context, context.getString(R.string.no_internet));
            return false;
        }

        /*if (!networkInfo.isFailover()) {
            Utils.shortToast(context, context.getString(R.string.no_internet));
            return false;
        }*/

        return true;
    }
}
