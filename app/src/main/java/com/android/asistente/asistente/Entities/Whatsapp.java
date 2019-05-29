package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.net.Uri;

public class Whatsapp {
    public void SendMessageTo(String contact, String message){
        try{
            Uri uri = Uri.parse("smsto:" + contact);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");

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
