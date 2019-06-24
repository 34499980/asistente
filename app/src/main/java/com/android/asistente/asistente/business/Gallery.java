package com.android.asistente.asistente.business;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;

public class Gallery extends AppCompatActivity {
    public void OpenGallery(){
        try{
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);}
        catch(Exception ex){
            Toast.makeText(this, "Error al abrir galeria", Toast.LENGTH_SHORT).show();
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
    }
}
