package com.android.asistente.asistente.business;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.R;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteservice;

import java.io.IOException;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Alarm  extends BroadcastReceiver {
    private MediaPlayer mMediaPlayer;
    private String timeAlarm;
    private String task;
    static AlarmManager alarmManager;
   static  PendingIntent pendingIntent;
   static  Intent intent;
   public static int time;
   public static String titulo;
   static String temporizador;
    public static void startAlertAtParticularTime() {

        // alarm first vibrate at 14 hrs and 40 min and repeat itself at ONE_HOUR interval

        intent = new Intent(asistenteservice.getContext(), Alarm.class);
         pendingIntent = PendingIntent.getBroadcast(
               asistenteservice.getContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 40);

        alarmManager= (AlarmManager) asistenteservice.getContext().getSystemService(ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, pendingIntent);

        Toast.makeText(asistenteservice.getContext(), "Alarm will vibrate at time specified",
                Toast.LENGTH_SHORT).show();

    }

    public static void startAlert() {

            intent = new Intent(asistenteservice.getContext(), Alarm.class);
            pendingIntent = PendingIntent.getBroadcast(
                    asistenteservice.getContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager = (AlarmManager) asistenteservice.getContext().getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + (time * 1000), 10000
                    , pendingIntent);

            Toast.makeText(asistenteservice.getContext(), "Alarm will set in " + time + " " +temporizador,
                    Toast.LENGTH_LONG).show();


    }

    @Override
    public void onReceive(Context context, Intent intent) {

       TTSService.speak("Recordatorio de "+titulo);
        alarmManager.cancel(pendingIntent);
    }
    public static void ProcesarDatosEntrada(String value) {

        try {

            if (value.toLowerCase().contains("recordarme en")) {
                if(value.toLowerCase().indexOf("recordarme en una") > -1){
                    time = (1 * 60)*60;
                    temporizador = "horas";
                }else if(value.toLowerCase().indexOf("recordarme en un") > -1) {
                    time = 1*60;
                    temporizador = "minutos";
                }else{
                 if(value.contains("minutos")) {
                     time = Integer.parseInt(value.substring(value.toLowerCase().indexOf("recordarme en") + 13, value.toLowerCase().indexOf("minuto")).trim()) * 60;
                     temporizador = "minutos";
                 }else{
                     time = (Integer.parseInt(value.substring(value.toLowerCase().indexOf("recordarme en") + 13, value.toLowerCase().indexOf("minuto")).trim()) * 60) *60;
                      temporizador = "horas";
                 }
                }
                titulo = value.substring(value.toLowerCase().indexOf("que tengo que") + 14);
            }


        } catch (Exception ex) {
            Log.appendLog("Alarm: " + ex.getMessage());

        }
    }

}
