package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

public class Sound extends BroadcastReceiver {
int val;

    //Using volume control UI visibility
//To increase media player volume
    public void setVolumen(int Volumen){
        try {
            val=(Volumen * 15) / 100;
            AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, val, 0);
        }catch (Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }
    public void setMusicVolumen(int Volumen){
        try {
            val=(Volumen * 15) / 100;
            AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0);
        }catch (Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }
    public static  int getVolume(){
        try {
            AudioManager audio = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
            return currentVolume;
        }catch(Exception ex){
            return -1;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())){
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            asistenteservice.startVoice();
        }

    }
}
