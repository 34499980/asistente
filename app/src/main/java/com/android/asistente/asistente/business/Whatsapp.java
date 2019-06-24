package com.android.asistente.asistente.business;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.MainActivity;

public class Whatsapp extends AppCompatActivity{
    public void SendMessageTo(String contact, String message){
        try{
            contact = contact.trim();
            contact =contact.substring(0,2).equals("549") ? contact.substring(3,contact.length()): contact;
            String url = "https://api.whatsapp.com/send?phone=+549" + ""+contact+"&text="+message;
            PackageManager pm = MainActivity.getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i .setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setData(Uri.parse(url));
            MainActivity.getContext().startActivity(i);
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
            return result;
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            return result;
        }
    }
}
