package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

public class RestarterTTS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {

           // Log.appendLog("Restarter"+"->"+"onReceive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, TTSService.class));
            } else {
                context.startService(new Intent(context, TTSService.class));
                MainActivity.bActive = true;
            }
          //  Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.appendLog(getClass().getName() + "->" + getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }

}
