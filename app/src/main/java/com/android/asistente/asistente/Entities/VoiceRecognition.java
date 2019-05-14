package com.android.asistente.asistente.Entities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceRecognition extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 1000;

    private SpeechRecognizer speech;
    static ArrayList<String> matches;
    RecognitionListener rec;
    Intent intent;
    Button btnPrueba;
    final Speech speek = new Speech();
    public static boolean listening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPrueba = (Button) findViewById(R.id.btnHablar);



    }
    public void OnInit(){
       // speech = SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
       // speech.setRecognitionListener(new listener());
    }
    class listener implements RecognitionListener{
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {

        }

        @Override
        public void onResults(Bundle data) {
            matches = data.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION);
            speech.stopListening();

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
    public void StartvoiceListening(){


        try {

            if (rec == null) {
                rec = new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle bundle) {
                        listening=false;
                        if(matches == null){
                            StartvoiceListening();
                        }
                    }

                    @Override
                    public void onBeginningOfSpeech() {

                    }

                    @Override
                    public void onRmsChanged(float v) {

                    }

                    @Override
                    public void onBufferReceived(byte[] bytes) {

                    }

                    @Override
                    public void onEndOfSpeech() {
                        listening=true;
                        if(matches == null){
                            StartvoiceListening();
                        }
                    }

                    @Override
                    public void onError(int i) {

                    }

                    @Override
                    public void onResults(Bundle data) {
                        matches = data.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION);
                        listening=true;
                        if (matches != null) {

                            if (matches.get(0).toLowerCase().equals("hola")) {


                                speek.speek("En que lo puedo ayudar");
                                StartvoiceListening();

                                VoiceRecognition.matches = null;
                                //onStartCommand(intent,0,2);
                                //voice.startVoiceInput();
                                // startTimer();


                            } else {
                                //Ejecuta los comandos
                                speek.speek("Lo siento, no tengo una respuesta");
                                StartvoiceListening();
                                //java.lang.RuntimeException: SpeechRecognizer should be used only from the application's main thread


                                VoiceRecognition.matches = null;
                            }


                        }else{
                            StartvoiceListening();
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {

                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {

                    }
                };
            }

            speech.setRecognitionListener(rec);
            speech.startListening(intent);
            listening = true;




        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
   public  void InitSpeech() {
        try {
            if(speech == null) {
                speech = SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
            }
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    MainActivity.getContext().getPackageName());
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{


            switch (requestCode) {
                case 1000: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        //mVoiceInputTv.setText(result.get(0));
                    }
                    break;
                }

            }
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> getMatches() {
        return matches;
    }
}
