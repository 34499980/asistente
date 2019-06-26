package com.android.asistente.asistente.business;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Timer;
import java.util.TimerTask;

public class asistenteservice extends Service implements TextToSpeech.OnInitListener {
    Timer timerObj=null;
     TimerTask timerTaskObj=null;
    static VoiceRecognition  voice  = new VoiceRecognition();
    static ArrayList<String> matches;
    static Context context;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intente, int flag,int idProcess){
        super.onStartCommand(intente, flag, idProcess);
       // Log.appendLog("asistenteSrvice"+"->"+"onStartComand");
        MainActivity.bActive = true;
        context = getApplicationContext();

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
            Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
          /*  Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);*/
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }

    }

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
        voice.InitSpeech();
        voice.StartvoiceListening();
    }

}
