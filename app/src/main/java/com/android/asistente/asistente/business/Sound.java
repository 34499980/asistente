package com.android.asistente.asistente.business;

import android.content.Context;
import android.media.AudioManager;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

public class Sound {
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
}
