package com.android.asistente.asistente;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;
import com.android.asistente.asistente.business.VoiceRecognition;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    VoiceRecognition voice = new VoiceRecognition();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      //  GradientDrawable shape =  new GradientDrawable();
     //   shape.setCornerRadius( 90 );






      //  findViewById(R.id.btnHablarWidget).setBackground(shape);
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds ){
          // voice.InitSpeech();
          // voice.StartvoiceListening();

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.btnHablarWidget,pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }
    @Override
    public void onReceive(Context context, Intent intent) {
       /* if(!VoiceRecognition.listening) {
            voice.InitSpeech();
            voice.StartvoiceListening();
            Toast.makeText(context, "Listening...", Toast.LENGTH_SHORT).show();
        }*/
        super.onReceive(context, intent);




    }
}
