package com.manish.geofences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context , Intent intent) {
        Toast.makeText(context , "Geofence is Triggered" , Toast.LENGTH_SHORT).show();
    }
}