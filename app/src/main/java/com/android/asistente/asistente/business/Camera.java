package com.android.asistente.asistente.business;

import android.content.Intent;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

import static androidx.core.content.ContextCompat.startActivity;

public class Camera{
    public static void OpenCammera() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(asistenteservice.getContext(), intent, null);
        }catch(Exception ex){
            Log.appendLog("Camera:"+ex.getMessage());
            throw ex;
        }
     }
}
