package com.android.asistente.asistente.Entities;

import android.content.Context;
import android.media.AudioManager;

import com.android.asistente.asistente.MainActivity;

public class Sound {


    //Using volume control UI visibility
//To increase media player volume
    public void setVolumen(int Volumen){
        try {
            AudioManager audioManager = (AudioManager) MainActivity.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Volumen, 0);
        }catch (Exception ex){
            throw ex;
        }
    }
}
