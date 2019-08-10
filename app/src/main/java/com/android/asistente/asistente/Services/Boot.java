package com.android.asistente.asistente.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.android.asistente.asistente.Helper.Log;

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
