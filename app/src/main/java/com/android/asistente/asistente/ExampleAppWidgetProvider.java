package com.android.asistente.asistente;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;
import com.android.asistente.asistente.business.VoiceRecognition;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    VoiceRecognition voice = new VoiceRecognition();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {







      //  findViewById(R.id.btnHablarWidget).setBackground(shape);
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds ){
          // voice.InitSpeech();
          // voice.StartvoiceListening();
          //  GradientDrawable shape =  new GradientDrawable();
          //  shape.setCornerRadius( 90 );
          //  shape.setColor(Color.parseColor("#ef5350"));//Color rojo
         //   LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         //   View view = inflater.inflate(R.layout.example_widget, null);
         //   Button buton = (Button)view.findViewById(R.id.btnHablarWidget);
            Intent intent = new Intent(context, MainActivity.class);
         //   view.findViewById(R.id.btnHablarWidget).setBackground(shape);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.btnHablarWidget,pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!VoiceRecognition.listening) {
            voice.InitSpeech();
            voice.StartvoiceListening();
            Toast.makeText(context, "Listening...", Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);




    }
}
