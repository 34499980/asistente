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
            voice.startVoiceInput();
            voice.voiceListening();
            startTimer();
         //   speech.startListening(intent);







        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
           // voice.voiceListening();
           // start = true;







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

                            if (matches.get(0).equals("hola")) {

                              //  voice.startVoiceInput();
                              //  voice = new VoiceRecognition();
                                voice.startVoiceInput();
                                voice.voiceListening();
                                //speech.startListening(intent);
                                VoiceRecognition.matches = null;
                            } else {


                                //voice = new VoiceRecognition();
                                voice.startVoiceInput();
                                voice.voiceListening();
                                VoiceRecognition.matches = null;
                            }

                        } else {
                          //  voice.voiceListening();

                           // speek.speek("no funciono");
                          //  speech.startListening(intent);

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
