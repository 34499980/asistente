package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

public class Whatsapp extends AppCompatActivity{
    public void SendMessageTo(String contact, String message){
        try{
            Uri uri = Uri.parse("smsto:" + contact);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent, "message"));

        }catch(Exception ex){

        }
    }
    public String ProcesarDatosEntrada(String value){
        String result="";
        if(value.toLowerCase().contains("whatsapp a")){
            result = value.substring(value.toLowerCase().indexOf("whatsapp a")+11);
        }
        return result;
    }
}
