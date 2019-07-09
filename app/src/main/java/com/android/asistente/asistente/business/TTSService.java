package com.android.asistente.asistente.business;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

import java.util.Locale;

/*public class Speech extends Activity{
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
    }
}*/
public class TTSService extends Service {

    private String str;
    private static TextToSpeech mTts;
    private static final String TAG="TTSService";
    public static boolean bSpeaking;
    public static Context context;
    static TTSService instance = null;


    public static TTSService getInstance(){
        if(instance == null){
            instance = new TTSService();
        }
        return instance;
    }

    @Override

    public IBinder onBind(Intent arg0) {

        return null;
    }


    @Override
    public void onCreate() {
        if(mTts == null){
            mTts = new TextToSpeech(this , new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.SUCCESS) {
                        mTts.setLanguage(Locale.getDefault());
                    }
                }
            });
        }

        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        try {
            // Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, RestarterTTS.class);
            this.sendBroadcast(broadcastIntent);
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
        super.onDestroy();
    }
    public void setContext(){
        context = this;
    }


    @Override
    public void onStart(Intent intent, int startId) {




       // Log.v(TAG, "onstart_service");
        super.onStart(intent, startId);
    }

   /* @Override
    public void onInit(int status) {
      //  Log.v(TAG, "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result =  mTts.setLanguage(Locale.getDefault());
            //int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
            //    Log.v(TAG, "Language is not available.");
            }
        } else {
          //  Log.v(TAG, "Could not initialize TextToSpeech.");
        }
    }*/
    public static void speak(String str) {

        if(mTts == null){
            mTts = new TextToSpeech( MainActivity.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.SUCCESS) {
                        mTts.setLanguage(Locale.getDefault());
                    }
                }
            });
        }
        mTts.speak(str, TextToSpeech.QUEUE_ADD, null);
        while (mTts.isSpeaking()) {
            bSpeaking = true;
        }
       // Log.appendLog("onResults Fin");
        bSpeaking = false;
    }
}
