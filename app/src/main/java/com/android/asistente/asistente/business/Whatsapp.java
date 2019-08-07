package com.android.asistente.asistente.business;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteservice;

import java.util.HashMap;
import java.util.Map;

public class Whatsapp extends AppCompatActivity{
    static Map<String, String> messages = new HashMap<String, String>();
    public void SendMessageTo(String contact, String message){
        try{
            contact = contact.trim();
            contact =contact.substring(0,5).equals("+54 9") ? contact.substring(5,contact.length()): contact;
            String url = "https://api.whatsapp.com/send?phone=+549" + ""+contact+"&text="+message;
            PackageManager pm = asistenteservice.getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i .setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setData(Uri.parse(url));
            asistenteservice.getContext().startActivity(i);
           /* Uri uri = Uri.parse("smsto:" + contact);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent, "message"));*/

        }catch(Exception ex){
            Toast.makeText(this, "No se pudo enviar el whatsapp", Toast.LENGTH_SHORT).show();
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
    }
    public String ProcesarDatosEntrada(String value){
        String result = "";
        try {

            if (value.toLowerCase().contains("whatsapp a")) {
                result = value.substring(value.toLowerCase().indexOf("whatsapp a") + 11);
            } else {
                result = value;
            }
            if(value.indexOf("leer whatsapp de ")> -1){
                result = value.substring(17,value.length());
            }
            return result;
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            return result;
        }
    }
    public static void putMessages(String user, String text){
        if(messages.size() == 0){
            messages.put(user,text+".");
        }else if(messages.size() == 10){
            if(messages.containsKey(user)){
                messages.put(user,  messages.get(user)+text+".");
            }else{
                messages.put(user,text+".");
            }

        }else{
            messages.clear();
            messages.put(user,text+".");
        }
    }
    public String getMessageByUser(String user){
        if(messages.containsKey(user)){
            return messages.get(user);
        }
        return "No tiene mensajes de ese usuario.";
    }
    public static void getAllMessage(){
        String key;
        String text;
        for (Map.Entry<String, String> entry : messages.entrySet()){
            TTSService.speak("Mensaje de "+ entry.getKey());
            TTSService.speak(entry.getValue());
        }
        if(messages.size() == 0){
            TTSService.speak("No tiene mensajes para leer.");
        }
        messages.clear();
    }
}
