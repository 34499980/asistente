package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteservice;

public class Battery extends BroadcastReceiver {
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = asistenteservice.getContext().registerReceiver(null, ifilter);
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    boolean usbCharge = chargePlug == BatteryManager. BATTERY_PLUGGED_USB;
    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    boolean warning = false;
    static Context context;

    int batteryPct = level / scale;

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

      //  Log.appendLog("Battery "+ String.valueOf(isCharging) +" usb: "+String.valueOf(usbCharge));
        if(!warning) {
            switch (status) {
                case BatteryManager.BATTERY_STATUS_FULL:
                    TTSService.speak("El dispositivo llego a la carga maxima.");
                    warning = true;
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    warning = true;
                    if (usbCharge) {
                        TTSService.speak("Iniciando carga por usb.");

                    } else {
                        TTSService.speak("Iniciando carga");
                    }
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    if (level == 15) {
                        warning = true;
                        TTSService.speak("Iniciando ahorro de bateria");
                    }
                    break;
            }
        }
      /*  if(isCharging){
            if(!warning){
                if(check().equals("")) {
                    if (usbCharge) {
                        TTSService.speak("Iniciando carga por usb.");
                    } else {
                        TTSService.speak("Iniciando carga");
                     }
                     warning = true;
                }
            }
        }else {

            String result = check();
            if(!result.equals(""))
            TTSService.speak(result);
        }*/
    }
    public int getLevel(){
         level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        // scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);


        // batteryPct = level / scale;

        return level;
    }
    public String check(){
        String result="";
        if(batteryPct == 15 && !warning){
            result="Iniciando ahorro de bateria";
            warning = true;
        }else if (batteryPct == 100){
            result = "El dispositivo llego a la carga máxima";
        }
        return result;
    }

}
