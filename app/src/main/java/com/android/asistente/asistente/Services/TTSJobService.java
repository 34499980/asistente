package com.android.asistente.asistente.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.android.asistente.asistente.MainActivity;

import java.util.Locale;

public class TTSJobService extends JobService {
    private String str;
    private static TextToSpeech mTts;
    private static final String TAG="TTSService";
    public static boolean bSpeaking;
    public static Context context;
    static TTSService instance = null;
    public static boolean bActive;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        context = this;
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
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
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
