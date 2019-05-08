package com.android.asistente.asistente.Entities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AsistenteService extends Service implements TextToSpeech.OnInitListener {
   //final Speech speech = new Speech();
    Timer timerObj=null;
     TimerTask timerTaskObj=null;
    boolean start;
    final Speech speek = new Speech();
    VoiceRecognition  voice  = new VoiceRecognition();
    private SpeechRecognizer speech =  SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
    static ArrayList<String> matches;
    Intent intent=null;
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
        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();
        try {
            if(intent == null){
                intent = intente;
            }
            voice.OnInit();
            //voice.voiceListening();
            startTimer();
         //   speech.startListening(intent);







        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }






            return START_STICKY;

    }
    @Override
    public void onDestroy(){
       timerObj.cancel();
        timerObj.purge();
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
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

                                try {
                                    speek.speek("En que lo puedo ayudar");
                                    Thread.sleep(3000);

                            }catch(InterruptedException e)
                            {
                                System.out.println("Thread Interrupted");
                            }
                            finally {

                                    VoiceRecognition.matches = null;
                                    //onStartCommand(intent,0,2);
                                    voice.startVoiceInput();

                                }

                            } else {
                                    //Ejecuta los comandos

                                        //java.lang.RuntimeException: SpeechRecognizer should be used only from the application's main thread


                                VoiceRecognition.matches = null;
                            }

                        } else {
                            //No escucho nada
                           // onStartCommand(intent,0,2);
                            voice.startVoiceInput();

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
