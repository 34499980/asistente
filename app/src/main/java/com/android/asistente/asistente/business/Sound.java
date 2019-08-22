package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
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
            Log.appendLog("Sound:"+ex.getMessage());
            throw ex;
        }
    }
    public void setMusicVolumen(int Volumen){
        try {
            val=(Volumen * 15) / 100;
            AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0);
        }catch (Exception ex){
            Log.appendLog("Sound:"+ex.getMessage());
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
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = (KeyEvent) intent
                .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_HEADSETHOOK:
                if (action == KeyEvent.ACTION_UP)
                    if (SystemClock.uptimeMillis() - event.getDownTime() > 2000)
                       asistenteservice.startVoice();
                break;
        }
        abortBroadcast();
    }
}
