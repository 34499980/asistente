package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;

import com.android.asistente.asistente.MainActivity;

import java.util.List;

public class ExternalApp extends AppCompatActivity {
    public ResolveInfo getAllAplication(String appName){
        ResolveInfo app = null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = MainActivity.getContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        for (ResolveInfo item:pkgAppsList) {
            if(item.activityInfo.packageName.toLowerCase().equals(appName)){
                app = item;
                break;
            }
        }
        return app;
    }
    public  String procesarDatosEntrada(String value){
        String result="";
        int index = value.indexOf("abrir aplicación");
        if(index > -1){
            result = value.substring(index+17);
        }
        return result;
    }
    public void startApp(ResolveInfo app){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app.activityInfo.packageName);
        startActivity( launchIntent );
    }
}
