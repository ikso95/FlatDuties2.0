package com.example.startactivity.BroadcastReceivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.startactivity.Activities.ManageDuties.EditCyclicalDuty;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.R;

public class NoInternetConnection extends BroadcastReceiver {

    //rowniez dodano w MainActivity
    //IntentFilter filter = new IntentFilter();
    //filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    //registerReceiver(new NoInternetConnection(), filter);
    //poniewaz od wersji androida 7 trzeba rejestrowac registerReceiver(BroadcastReceiver,IntentFilter)


    private boolean isInternetConnected=true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {

                if(isInternetConnected==false)
                {
                    isInternetConnected=true;
                    Intent connectionIntent = new Intent(context, MainActivity.class);
                    connectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(connectionIntent);
                }


            } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                isInternetConnected=false;
                Intent noConnectionIntent = new Intent(context,NoInternetDisplay.class);
                noConnectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(noConnectionIntent);
            }
        }
    }

    /*
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {
                // phone network connect success


                Intent noConnectionIntent = new Intent(context, MainActivity.class);
                noConnectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(noConnectionIntent);


                Toast.makeText(context,"Internet connection on", Toast.LENGTH_LONG).show();
            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
                // no network

                Intent noConnectionIntent = new Intent(context,NoInternetDisplay.class);
                noConnectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(noConnectionIntent);

                Toast.makeText(context,"NO Internet connection", Toast.LENGTH_LONG).show();
            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                // wift connect success
                Intent noConnectionIntent = new Intent(context, MainActivity.class);
                noConnectionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(noConnectionIntent);

                Toast.makeText(context,"Internet WIFI connection on", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}


