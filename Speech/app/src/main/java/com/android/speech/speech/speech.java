package com.android.speech.speech;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Locale;


public  class speech extends AppCompatActivity {

static TextToSpeech toSpeech;

 public static void newspeech(){
     if(toSpeech == null){
         toSpeech = new TextToSpeech(MainActivity.getContext(), new TextToSpeech.OnInitListener() {
             @Override
             public void onInit(int status) {
                 if(status == TextToSpeech.SUCCESS){
                     toSpeech.setLanguage(Locale.getDefault());
                 }
             }
         });
     }
 }
 public static void Hablar(String texto){
     newspeech();
     toSpeech.speak(texto,TextToSpeech.QUEUE_FLUSH,null);
 }

}
