package com.android.asistente.asistente.business;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;

import java.util.Locale;

public class Speech extends Activity{
    private TextToSpeech tts;
    public static boolean bSpeaking;
    public void speech(){
        try {
             tts = new TextToSpeech(MainActivity.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.getDefault());
                    }
                }
            });
           // speek("Servicio iniciado");

        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
public void speek(String text){
        if(tts == null) {
          speech();
        }
    tts.speak(text, TextToSpeech.QUEUE_ADD, null);
    while(tts.isSpeaking()){
        bSpeaking = true;
    }
    bSpeaking = false;
}
   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
    }*/
}
