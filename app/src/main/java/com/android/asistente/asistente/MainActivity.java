package com.android.asistente.asistente;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.NotificationService;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteservice;
import com.android.asistente.asistente.business.VoiceRecognition;

import java.util.Timer;
import java.util.TimerTask;

//import com.android.asistente.asistente.business;
//import com.android.asistente.asistente.business.CalendarsAs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   public static Button btnStartService, btnStopService;
    Button btHablar;
    static Context context;
    //TTSService speech = TTSService.getInstance();
    GradientDrawable shape;
    VoiceRecognition voice = new VoiceRecognition();
    Timer timerObj ;
    TimerTask timerTaskObj;
    int countTimer;
    public static Button  buttonWidget = null;
    View view;
    public static Boolean bActive = false;
    static MainActivity instance = null;

   /* private MainActivity(){};
    public static MainActivity getInstance(){
        if (instance == null){
            instance = new MainActivity();
        }
        return instance;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        try {
            CargarComponentes();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.example_widget, null);
            buttonWidget = (Button) view.findViewById(R.id.btnHablarWidget);
        }catch(Exception ex){
            Log.appendLog("MainActivity:" + ex.getMessage());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void CargarComponentes() {
        try {
           // new asistenteservice();
            //startService(new Intent(this, asistenteservice.class));
            shape = new GradientDrawable();
            shape.setCornerRadius(90);
           // speech.speek("Iniciando");
            shape.setColor(Color.parseColor("#81c784"));


            findViewById(R.id.btnHablar).setBackground(shape);

            btnStartService = (Button) findViewById(R.id.btnStartService);
            btnStopService = (Button) findViewById(R.id.btnStopService);
            btHablar = (Button) findViewById(R.id.btnHablar);
            btnStartService.setOnClickListener(this);

            btnStopService.setOnClickListener(this);
            btHablar.setOnClickListener(this);
            btnStartService.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                    return false;
                }
            });
            startTimer();
        }catch(Exception ex){
            Log.appendLog("MainActivity:" + ex.getMessage());
        }
      //  speech.speek("");
        //Fuerzo el click de hablar.
        //btHablar.performClick();
    }

    @Override
    public void onClick(View view) {
        try {
        switch (view.getId()){
            case R.id.btnStartService:

                bActive = true;



                //Inicio del servicio principal del asistente
                asistenteservice.bActive= true;
                ContextCompat.startForegroundService(this,new Intent(this, asistenteservice.class));

               //Agrego la observer la llegada de notificaciones
                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
                ncomp.setContentTitle("My Notification");
                ncomp.setContentText("Notification Listener Service Example");
                ncomp.setTicker("Notification Listener Service Example");
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
                //Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
               // startActivity(intent);


                break;
            case R.id.btnStopService:
                bActive = false;
                TTSService.speak("Servicio detenido");
               // NotificationService.bNotify=false;
                asistenteservice.bActive = false;
                stopService(new Intent(this,NotificationService.class));
                stopService(new Intent(this,asistenteservice.class));
                break;
            case R.id.btnHablar:

                shape.setColor(Color.parseColor("#ef5350"));//Color rojo

                 voice.InitSpeech();
                voice.StartvoiceListening();
                break;
        }
        }catch(Exception ex){
            Log.appendLog("MainActivity:" + ex.getMessage());
            Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    public  Context getNotStaticContext(){
        return this;
    }
    public static Context getContext(){
        return context;
    }
    public void startTimer(){
        try {

            timerObj = new Timer();
            timerTaskObj = new TimerTask() {
                public void run() {
                   /* if((voice.listening && voice.getMatches()!=null) || Speech.bSpeaking){
                        shape.setColor(Color.parseColor("#ef5350"));//Color rojo
                    }else{
                        shape.setColor(Color.parseColor("#81c784"));//Color verde
                        voice.listening=false;
                       // buttonWidget.performClick();

                    }*/

                    if(voice.listening ||  TTSService.bSpeaking){
                        shape.setColor(Color.parseColor("#ef5350"));//Color rojo
                        if(voice.getMatches()==null) {
                            countTimer++;
                        }
                        if(countTimer==3){
                            countTimer=0;
                         voice.listening=false;

                        }
                    }else{
                        try {
                         //   buttonWidget.performClick();
                        }catch(Exception ex){

                        }
                        shape.setColor(Color.parseColor("#81c784"));//Color verde

                    }

                    findViewById(R.id.btnHablar).setBackground(shape);

                }
            };
            try {
                timerObj.schedule(timerTaskObj, 0, 1000);
            }catch(Exception ex){
                Log.appendLog("MainActivity:" + ex.getMessage());
                throw ex;
            }


        }catch(Exception ex){
            Log.appendLog("MainActivity:" + ex.getMessage());
            throw ex;
        }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {

                    return true;
                }
            }

            return false;

        } catch (Exception ex) {
            Log.appendLog("MainActivity:" + ex.getMessage());
            throw ex;
        }
    }

}
