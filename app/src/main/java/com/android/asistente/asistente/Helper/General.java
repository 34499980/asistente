package com.android.asistente.asistente.Helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;


import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.Services.asistenteservice;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class General extends Activity {
 public static List<Phone> list;
 public static Phone selectedPhone;
 public static String CleanString(String cadena) {
  String limpio =null;
  if (cadena !=null) {
   String valor = cadena;
   valor = valor.toUpperCase();
   // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
   limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
   // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
   limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
   // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
   limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC).replace('k','c');
  }
  return limpio;
 }
    public static String CleanStringFromHTML(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("<BR>","").replaceAll("<B>","").replaceAll("</B>","")
                    .replaceAll("\n","").replaceAll("&NBSP","");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores

        }
        return limpio;
    }
 public  boolean isMyServiceRunning(Class<?> serviceClass) {
     try {
         //ConnectivityManager manager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
         for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
             if (serviceClass.getName().equals(service.service.getClassName())) {
                 return true;
             }
         }
         return false;
     }catch(Exception ex){
         Log.appendLog("General:"+ex.getMessage());
         throw ex;
     }
 }
 public void startService(Class<?> serviceClass){
    try {
        new MainActivity();
        MainActivity.getContext().startService(new Intent(MainActivity.getContext(), serviceClass));
    }catch(Exception ex){
      //  Log.appendLog("General:"+ex.getMessage());
    }
 }
 public static boolean isHeadSetConnect(){
     try {
         AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
         return audioManager.isWiredHeadsetOn();
     }catch(Exception ex){
         Log.appendLog("isHeadSetConnect: "+ex.getMessage());
         return false;
     }
 }
 public static void enabledDesabledWifi(boolean status){
     WifiManager wifiManager = (WifiManager)asistenteservice.getContext().getSystemService(Context.WIFI_SERVICE);
     wifiManager.setWifiEnabled(status);
 }
    public static String enabledDesabledBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
            return "desactivado";
        }else{
            bluetoothAdapter.enable();
            return "activado";
        }
    }
    public static boolean isBluetoothEnabled(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       return bluetoothAdapter.isEnabled();
    }
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
