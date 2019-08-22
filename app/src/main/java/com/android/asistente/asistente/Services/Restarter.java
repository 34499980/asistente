package com.android.asistente.asistente.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {

           // Log.appendLog("Restarter"+"->"+"onReceive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, asistenteservice.class));
            } else {
                context.startForegroundService(new Intent(context, asistenteservice.class));
               // context.startService(new Intent(context, asistenteservice.class));
            }
          //  Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.appendLog("Restarter:"+ex.getMessage());
            throw ex;
        }
    }

}
