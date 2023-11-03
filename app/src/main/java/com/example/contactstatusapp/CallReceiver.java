package com.example.contactstatusapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            SharedPreferences sharedPreferences = context.getSharedPreferences("STATUS_HANDLER", Context.MODE_PRIVATE);
            boolean isOn = sharedPreferences.getBoolean("isOn", false);
            if(isOn){
                String status = sharedPreferences.getString("status", "");
                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
            }
        }
    }
}
