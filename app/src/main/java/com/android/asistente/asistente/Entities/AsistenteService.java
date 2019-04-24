package com.android.asistente.asistente.Entities;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AsistenteService extends Service implements TextToSpeech.OnInitListener {
    Speech speech = new Speech();
    Timer timer = new Timer();
    boolean start;
   final VoiceRecognition  voice  = new VoiceRecognition();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        PruebaAudio();
    }

    @Override
    public int onStartCommand(Intent intente, int flag,int idProcess){
        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_SHORT).show();
        try {
            voice.startVoiceInput();
            voice.voiceListening();
            start = true;
          /*  CountDownTimer waitTimer;
            waitTimer = new CountDownTimer(60000, 300) {

                public void onTick(long millisUntilFinished) {
                    ArrayList<String> matches = voice.getMatches();
                    if (matches != null) {
                        cancel();
                        if (matches.get(0).equals("hola")) {
                            PruebaAudio("En que lo puedo ayudar");
                            // voice.startVoiceInput();
                            // voice.voiceListening();
                            VoiceRecognition.matches = null;
                        } else {
                            // voice.startVoiceInput();
                            // voice.voiceListening();
                            VoiceRecognition.matches = null;
                        }
                    }
                }

                public void onFinish() {
                    //After 60000 milliseconds (60 sec) finish current
                    //if you would like to execute something when time finishes
                }
            }.start();*/





            return START_STICKY;
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            throw ex;
        }
    }
    @Override
    public void onDestroy(){

        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInit(int i) {
        PruebaAudio();
    }
    public void PruebaAudio(){
        if(start){
          timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ArrayList<String> matches = voice.getMatches();
                    if (matches != null) {
                        if (matches.get(0).equals("hola")) {

                             voice.startVoiceInput();
                             voice.voiceListening();
                            VoiceRecognition.matches = null;
                        } else {
                             voice.startVoiceInput();
                             voice.voiceListening();
                            VoiceRecognition.matches = null;
                        }

                    }
                }
            },0,3000);
        }
    }
}
