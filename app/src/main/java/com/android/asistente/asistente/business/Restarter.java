package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, asistenteservice.class));
            } else {
                context.startService(new Intent(context, asistenteservice.class));
            }

        } catch (Exception ex) {
            Log.appendLog(getClass().getName() + "->" + getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }

}
