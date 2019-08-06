package com.example.startactivity.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.startactivity.Models.User;

public class Common {

    public static User currentUser;

    //public static String url="http://192.168.0.178:8888/api/";  //wroclaw
    public static String url="http://192.168.1.58:8888/api/"; //opole

    public static String img_url="http://192.168.0.178:8888/public/";

    public static String getUrl() {
        return url;
    }
    public static String getImgUrl() {
        return img_url;
    }



    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
