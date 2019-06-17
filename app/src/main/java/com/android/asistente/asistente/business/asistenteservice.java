package com.android.asistente.asistente.business;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class asistenteservice extends Service implements TextToSpeech.OnInitListener {
   //final Speech speech = new Speech();
    Timer timerObj=null;
     TimerTask timerTaskObj=null;
    boolean start;
    final Speech speek = new Speech();
    VoiceRecognition  voice  = new VoiceRecognition();
    private SpeechRecognizer speech =  SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
    static ArrayList<String> matches;
    Intent intent=null;
    boolean flag;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intente, int flag,int idProcess){
       // android.os.Debug.waitForDebugger();
        Log.appendLog("Servicio iniciado");
        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();
        try {

           // voice.OnInit();

           // startTimer();
            //new VoiceRecognition().StartvoiceListening();







        }catch(Exception ex){
            Log.appendLog("onStartCommand "+ ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }






            return START_STICKY;

    }
    @Override
    public void onDestroy(){
        Log.appendLog("Deteniendo el servicio");
       timerObj.cancel();
        timerObj.purge();
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
        Log.appendLog("Servicio detenido");
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

}
