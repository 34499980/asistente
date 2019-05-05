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
    // private TextView mVoiceInputTv;
    // private ImageButton mSpeakBtn;
    private SpeechRecognizer speech;
    private Intent mSpeechRecognizerIntent;
    private ListView wordsList;
    static ArrayList<String> matches;
    RecognitionListener rec;
    final Speech speek = new Speech();

    Intent intent;
    Button btnPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPrueba = (Button) findViewById(R.id.btnHablar);

        //  mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        //   mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        //  mSpeakBtn.setOnClickListener(new View.OnClickListener() {

    }
public void voiceListening(){


    try {

                if (rec == null) {
                    rec = new RecognitionListener() {
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





    }catch(Exception ex){
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
    void startVoiceInput() {
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
