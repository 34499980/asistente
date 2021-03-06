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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.business.Time;
import com.android.asistente.asistente.business.VoiceRecognition;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class asistenteservice extends Service implements TextToSpeech.OnInitListener {
    Timer timerObj=null;
     TimerTask timerTaskObj=null;
    static VoiceRecognition voice  = new VoiceRecognition();
    static ArrayList<String> matches;
    static Context context;
    public static boolean bActive;



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

            }
        }catch(Exception ex){
            Log.appendLog(ex.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intente, int flag,int idProcess){
        super.onStartCommand(intente, flag, idProcess);
       // Log.appendLog("asistenteSrvice"+"->"+"onStartComand");
        bActive = true;
        context = getApplicationContext();
        startService(new Intent(this, TTSService.class));
      //  Time time = Time.getInstance();
       // time.getTemperatureNow();
       // startForeground(idProcess,null);

       // startService(new Intent(this, NotificationService.class));

        try {

        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
