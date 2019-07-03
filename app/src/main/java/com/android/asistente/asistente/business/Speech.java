package com.android.asistente.asistente.business;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

import java.util.Locale;

public class Speech extends Activity{
    private TextToSpeech tts;
    MainActivity main = null;
    public static boolean bSpeaking;

    private void speech(){
        try {
            if (main == null){

               main = new MainActivity();
                Log.appendLog("Crea Main");
            }

             tts = new TextToSpeech( main.getNotStaticContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.getDefault());
                    }
                }
            });
           // speek("Servicio iniciado");

        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName()+"->"+ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
public void speek(String text){
    Log.appendLog("speek inicio->"+text);
        try {
            if (tts == null) {
                speech();
            }
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            while (tts.isSpeaking()) {
                bSpeaking = true;
            }
            Log.appendLog("onResults Fin");
            bSpeaking = false;
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
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
