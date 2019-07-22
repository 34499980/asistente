package com.android.asistente.asistente.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import com.android.asistente.asistente.business.Time;
import com.android.asistente.asistente.business.VoiceRecognition;

public class asistenteJobService extends JobService {
    public static Context context;
    public static boolean bActive;
    static VoiceRecognition voice;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        context  = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                voice = new VoiceRecognition();
                bActive = true;
                context = getApplicationContext();
              //  Time time = Time.getInstance();
               // time.getTemperatureNow();
            }
        }).start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
    public static  Context getContext(){
        return context;
    }
    public static void startVoice(){
        //  Log.appendLog("startVoice inicio");
        voice.InitSpeech();
        voice.StartvoiceListening();
        //  Log.appendLog("startVoice fin");
    }
}
