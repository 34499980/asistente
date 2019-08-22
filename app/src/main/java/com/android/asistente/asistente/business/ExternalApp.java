package com.android.asistente.asistente.business;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

import java.util.List;

public class ExternalApp extends AppCompatActivity {
    public ResolveInfo getAllAplication(String appName){
        try {
            ResolveInfo app = null;
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pkgAppsList = asistenteservice.getContext().getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo item : pkgAppsList) {
                if (item.activityInfo.packageName.toLowerCase().contains(appName.trim())) {
                    app = item;
                    break;
                }
            }
            return app;
        }catch(Exception ex){
            Log.appendLog("ExternalApp:"+ex.getMessage());
            throw ex;
        }
    }
    public  String procesarDatosEntrada(String value){
        try {
            String result = "";
            value = value.toLowerCase().replace("abrir aplicación de", "abrir");
            int index = value.indexOf("abrir");
            if (index > -1) {
                result = value.substring(index + 5)
                        .replace("movimientos", "hola")
                        .replace("contactos", "contacts")
                        .replace("galería", "photo")
                        .replace("calendario", "calendar")
                        .replace("música", "music")
                        .replace("cámara", "cam")
                        .replace("calculadora", "calculator");
            }
            return result;
        }catch(Exception ex){
            Log.appendLog("ExternalApp:"+ex.getMessage());
            throw ex;
        }
    }
    public void startApp(ResolveInfo app){
        try {
            if(app != null) {
                Intent launchIntent = asistenteservice.getContext().getPackageManager().getLaunchIntentForPackage(app.activityInfo.packageName);
                asistenteservice.getContext().startActivity(launchIntent);
            }
        }catch(Exception ex){
            Toast.makeText(this, "No se pudo abrir la app", Toast.LENGTH_SHORT).show();
            Log.appendLog("ExternalApp:"+ex.getMessage());
        }
    }
}
