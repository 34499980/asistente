package com.android.asistente.asistente.Services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Boot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            try {
                ContextCompat.startForegroundService(context, new Intent(context, asistenteservice.class));
            }catch(Exception ex){
                Log.appendLog("Boot: "+ex.getMessage());
            }
        }

    }
}
