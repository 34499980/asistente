package com.android.asistente.asistente.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;
import com.android.asistente.asistente.business.Sound;
import com.android.asistente.asistente.business.Time;
import com.android.asistente.asistente.business.VoiceRecognition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class asistenteservice extends Service implements TextToSpeech.OnInitListener {
    Timer timerObj=null;
     TimerTask timerTaskObj=null;
    static VoiceRecognition voice  = new VoiceRecognition();
    static ArrayList<String> matches;
    static Context context;
    public static boolean bActive;
    static boolean listening=false;
    boolean firstTime = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate(){

        super.onCreate();
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                String CHANNEL_ID = "my_channel_01";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("")
                        .setContentText("").build();

                startForeground(1, notification);
                asistenteservice.bActive= true;
                ContextCompat.startForegroundService(this,new Intent(this, asistenteservice.class));

                Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                //// startActivity(intent);
                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(MainActivity.getContext());
                ncomp.setContentTitle("My Notification");
                ncomp.setContentText("Notification Listener Service Example");
                ncomp.setTicker("Notification Listener Service Example");
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
                //Deberia hablar cuando se inicia el servicio
                /*
                Calendar cal = Calendar.getInstance();
                String Hour = String.valueOf(cal.get(Calendar.HOUR));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                int hour = Integer.parseInt(simpleDateFormat.format(new Date()));
                while(TTSService.context != null) {
                }

                if ((General.isHeadSetConnect() || Sound.getVolume() > 0 && !firstTime)&& hour < 12) {
                    firstTime = true;
                    TTSService.speak("");
                    Time time = Time.getInstance();
                    time.getTemperatureNow("dia");
                }*/


            }
        }catch(Exception ex){
          //  Log.appendLog(ex.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intente, int flag,int idProcess) {
        super.onStartCommand(intente, flag, idProcess);
        // Log.appendLog("asistenteSrvice"+"->"+"onStartComand");
        bActive = true;
        context = getApplicationContext();
        //Inicio del servicio para hablar
        startService(new Intent(this, TTSService.class));
        //Inicio del servicio de notificaciones
        startService(new Intent(this, NotificationService.class));

        try {

        }catch(Exception ex){
            Log.appendLog("asistenteservice:"+ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }






            return START_STICKY;

    }
    @Override
    public void onDestroy(){
        try {
            if(asistenteservice.bActive) {
                // Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("restartservice");
                broadcastIntent.setClass(this, Restarter.class);
                this.sendBroadcast(broadcastIntent);
            }
        }catch(Exception ex){
            Log.appendLog("asistenteservice:"+ex.getMessage());
        }

    }

  /*  @Override
    protected void onHandleWork(@NonNull Intent intent) {
        MainActivity.bActive = true;
        context = getApplicationContext();
        Time time = Time.getInstance();
        time.getTemperatureNow();
    }*/

    @Override
    public void onInit(int i) {

    }
    public void startTimer(){
        try {

                timerObj = new Timer();
                timerTaskObj = new TimerTask() {
                    public void run() {

                        matches = voice.getMatches();
                        if (matches != null) {

                            if (matches.get(0).toLowerCase().equals("hola")) {

                                timerObj.cancel();
                                timerObj.purge();

                                startTimer();


                            } else {
                                    //Ejecuta los comandos

                                        //java.lang.RuntimeException: SpeechRecognizer should be used only from the application's main thread


                               // VoiceRecognition.matches = null;
                            }

                        }

                    }
                };
                try {
                    timerObj.schedule(timerTaskObj, 0, 5000);
                }catch(Exception ex){
                    throw ex;
                }


        }catch(Exception ex){
            throw ex;
        }

        }
    public static  Context getContext(){
         return context;
        }
    public static void startVoice(){
      //
        if(asistenteservice.bActive) {
            voice.InitSpeech();
            voice.StartvoiceListening();
            //  Log.appendLog("startVoice fin");
        }
    }

}
