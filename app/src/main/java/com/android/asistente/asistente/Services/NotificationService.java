package com.android.asistente.asistente.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.appcompat.widget.ThemedSpinnerAdapter;

import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.business.Sound;
import com.android.asistente.asistente.business.Whatsapp;


public class NotificationService extends NotificationListenerService {
    private boolean flag=true;
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    public static boolean bActivate=true;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            if(bActivate) {
                String text="";
                String pack = sbn.getPackageName() != null ? sbn.getPackageName().toLowerCase() : null;
                Bundle extras = sbn.getNotification().extras;
                String title = extras.getString("android.title") != null ? extras.getString("android.title").toLowerCase() : null;
                CharSequence textSequence = extras.getCharSequence("android.text");
                if (textSequence != null) {
                    text = textSequence.toString();
                }

                if (flag){
                    if (pack.contains("whatsapp") || pack.contains("facebook") || pack.contains("instagram") || pack.contains("com.google.android.gm")) {
                        if (asistenteservice.bActive && !title.equals("whatsapp")) {
                            switch (pack.toLowerCase()) {
                                case "com.google.android.gm":
                                    TTSService.speak("Ha recibido un mail.");
                                    break;
                                case "com.facebook.katana":
                                    if(text.contains("cumple") || text.contains("muro") || text.contains("en tu biografía")|| text.toLowerCase().contains("alerta") && !title.contains("burbujas de chat")){
                                        text = text.toLowerCase().substring(text.toLowerCase().indexOf("hoy"));
                                        TTSService.speak(text);
                                    }

                                    break;
                                default:
                                    if(General.isHeadSetConnect() || Sound.getVolume() > 0) {
                                        if(text.equals("📷 Foto")){
                                            TTSService.speak(  title + " le ha enviado una foto por whatsapp");
                                        }else{
                                            TTSService.speak("Mensaje de " + title);                                        }

                                        if(pack.contains("whatsapp")) {
                                            Whatsapp.putMessages(title, text);
                                        }
                                    }

                                    break;

                            }

                        }
                        // Log.appendLog(title + ": " + text);
                        flag = false;
                    }
                } else {
                    flag = true;
                }
                //  Log.i(TAG,"**********  onNotificationPosted");
                // Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
                Intent i = new Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                i.putExtra("notification_event", "onNotificationPosted :" + sbn.getPackageName() + "n");
                sendBroadcast(i);
            }

        }catch(Exception ex){
          //  Log.appendLog("NotificationService" +ex.getMessage());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //Log.i(TAG,"********** onNOtificationRemoved");
        //Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
        Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "n");

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NotificationService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NotificationService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }
    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event");
            Log.appendLog(temp);

            // txtView.setText(temp);
        }
    }
    public static void ActivateService(){
        if(!bActivate){
            bActivate = true;
            TTSService.speak("Servicio de notificaciones activadas.");
        }else {
            TTSService.speak("El servicio ya se encuentra activado.");
        }
    }
    public static  void DesactivateService(){
        if(bActivate) {
            bActivate = false;
            TTSService.speak("Servicio de notificaciones desactivada.");
        }else {
            TTSService.speak("El servicio ya se encuentra desactivado.");
        }
    }
}