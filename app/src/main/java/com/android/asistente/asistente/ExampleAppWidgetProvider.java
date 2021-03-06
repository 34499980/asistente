package com.android.asistente.asistente;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.business.VoiceRecognition;
import com.android.asistente.asistente.Services.asistenteservice;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    VoiceRecognition voice = new VoiceRecognition();
    static String CLICK_ACTION = "CLICKED";
    MainActivity main;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        try {
           /* main = MainActivity.getInstance();
            main.startTimer();*/
        }catch(Exception ex){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        for (int appWidgetId : appWidgetIds ){

            Intent intent = new Intent(context, ExampleAppWidgetProvider.class);
            intent.setAction(CLICK_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.btnHablarWidget,pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(CLICK_ACTION)){
            try {
                //Me fijo si estan activos los servicios
               /* General general = new General();
                if(!asistenteservice.bActive){
                    general.startService(asistenteservice.class);

                }
                if(!TTSService.bActive){
                    general.startService(TTSService.class);
                }*/
                if(!VoiceRecognition.listening) {
                    asistenteservice.startVoice();

                   /* voice.InitSpeech();
                    voice.StartvoiceListening();
                    Toast.makeText(context, "Listening...", Toast.LENGTH_SHORT).show();*/
                }
            }catch(Exception ex){
                Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
                Log.appendLog( ex.getMessage());
            }

        }




    }
}
