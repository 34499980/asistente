package com.android.asistente.asistente;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.android.asistente.asistente.Entities.AsistenteService;
import com.android.asistente.asistente.Entities.Speech;
import com.android.asistente.asistente.Entities.VoiceRecognition;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartService, btnStopService;
    Button btHablar;
    static Context context;
    Speech speech = new Speech();
    GradientDrawable shape;
    VoiceRecognition voice = new VoiceRecognition();
    Timer timerObj ;
    TimerTask timerTaskObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        context = getApplicationContext();
        CargarComponentes();


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
        shape =  new GradientDrawable();
        shape.setCornerRadius( 90 );

       shape.setColor(Color.parseColor("#81c784"));




        findViewById(R.id.btnHablar).setBackground(shape);

        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStopService = (Button)findViewById(R.id.btnStopService);
        btHablar = (Button)findViewById(R.id.btnHablar);
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btHablar.setOnClickListener(this);
        startTimer();
        speech.speek("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStartService:
                speech.speek("Servicio Iniciado");
                startService(new Intent(this, AsistenteService.class));
                break;
            case R.id.btnStopService:
                speech.speek("Servicio detenido");
                stopService(new Intent(this,AsistenteService.class));
                break;
            case R.id.btnHablar:

                shape.setColor(Color.parseColor("#ef5350"));
                voice.InitSpeech();
                voice.StartvoiceListening();





                break;
        }

    }
    public static Context getContext(){
        return context;
    }
    public void startTimer(){
        try {

            timerObj = new Timer();
            timerTaskObj = new TimerTask() {
                public void run() {
                    if(voice.listening == true){
                        shape.setColor(Color.parseColor("#ef5350"));
                    }else{
                        shape.setColor(Color.parseColor("#81c784"));
                    }
                    findViewById(R.id.btnHablar).setBackground(shape);
                }
            };
            try {
                timerObj.schedule(timerTaskObj, 0, 3000);
            }catch(Exception ex){
                throw ex;
            }


        }catch(Exception ex){
            throw ex;
        }

    }
}
