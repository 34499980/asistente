package com.android.asistente.asistente.business;

import android.content.Intent;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

import static android.support.v4.content.ContextCompat.startActivity;

public class Camera{
    public static void OpenCammera() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(MainActivity.getContext(), intent, null);
        }catch(Exception ex){
            Log.appendLog(Camera.class.getName()+"->"+ Camera.class.getEnclosingMethod().getName());
            throw ex;
        }
     }
}
