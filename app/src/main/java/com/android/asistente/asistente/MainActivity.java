package com.android.asistente.asistente;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.NotificationService;
//import com.android.asistente.asistente.business;
import com.android.asistente.asistente.Services.TTSJobService;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteJobService;
import com.android.asistente.asistente.business.VoiceRecognition;
import com.android.asistente.asistente.Services.asistenteservice;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.PendingIntent.getActivity;

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
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
            startTimer();
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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




                asistenteservice.bActive= true;
                ContextCompat.startForegroundService(this,new Intent(this, asistenteservice.class));

                Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
                ncomp.setContentTitle("My Notification");
                ncomp.setContentText("Notification Listener Service Example");
                ncomp.setTicker("Notification Listener Service Example");
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
                /* BroadcastReceiver onNotice= new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String pack = intent.getStringExtra("package");
                        String title = intent.getStringExtra("title");
                        String text = intent.getStringExtra("text");



                      /*  TableRow tr = new TableRow(getApplicationContext());
                        tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        TextView textview = new TextView(getApplicationContext());
                        textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                        textview.setTextSize(20);
                        textview.setTextColor(Color.parseColor("#0B0719"));
                        textview.setText(Html.fromHtml(pack +"<br><b>" + title + " : </b>" + text));
                        tr.addView(textview);
                        tab.addView(tr);




                    }
                };*/
               // TTSService.speak("Servicio Iniciado");

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
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
                Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
                throw ex;
            }


        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
            Log.appendLog(getClass().getName() + "->" + getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }

}
