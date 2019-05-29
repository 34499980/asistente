package com.android.asistente.asistente.Entities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VoiceRecognition extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 1000;

    private SpeechRecognizer voice;
    static ArrayList<String> matches;
    RecognitionListener rec;
    Intent intent;
    Button btnPrueba;
    Whatsapp whatsapp = new Whatsapp();
    ExternalApp externalApp = new ExternalApp();
    Contacts contacts = new Contacts();
    Gallery gallery = new Gallery();
    boolean bFlag= false;
    boolean bConfirm = false;
    final Speech speech = new Speech();
    Sound sound = new Sound();
    String appName;
    String message;
    public static boolean listening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPrueba = (Button) findViewById(R.id.btnHablar);



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
                        listening=false;
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
                        listening=false;
                        if (matches != null) {

                            if (matches.get(0).toLowerCase().equals("hola")) {


                                speech.speek("En que lo puedo ayudar");



                            } else {
                                String letters=null;
                                if(!bFlag) {
                                    if (matches.get(0).toLowerCase().indexOf("volumen") > -1) {
                                        letters = "volumen";
                                    } else if (matches.get(0).toLowerCase().indexOf("cámara") > -1) {
                                        letters = "cámara";
                                    } else if (matches.get(0).toLowerCase().contains("whatsapp")) {
                                        letters = "whatsapp";
                                    } else if (matches.get(0).toLowerCase().indexOf("llamar") > -1 || matches.get(0).toLowerCase().indexOf("mensaje") > -1) {
                                        letters = "contacto";
                                    } else if (matches.get(0).toLowerCase().indexOf("abrir") > -1) {
                                        letters = "ExternalApp";
                                    } else {
                                        letters = matches.get(0).toLowerCase();
                                    }
                                }else{
                                    letters = "whatsapp";
                                }
                                //Ejecuta los comandos
                                switch (letters){
                                    case "qué hora es":
                                        Calendar cal = Calendar.getInstance();
                                        String Hour = String.valueOf(cal.get(Calendar.HOUR));
                                        String Minutes = String.valueOf(cal.get(Calendar.MINUTE));
                                        speech.speek("Son las "+ Hour + " y "+ Minutes);
                                        break;
                                    case "ExternalApp":
                                      appName =  externalApp.procesarDatosEntrada(matches.get(0).toLowerCase());
                                      ResolveInfo app = externalApp.getAllAplication(appName);
                                      externalApp.startApp(app);
                                        break;
                                    case "whatsapp":
                                        if(!bFlag) {
                                            appName = whatsapp.ProcesarDatosEntrada(matches.get(0).toLowerCase());
                                            if (appName.isEmpty()) {
                                                bFlag = true;
                                                speech.speek("a quien desea enviar mensaje");
                                            }else{
                                                bFlag = true;
                                                speech.speek("que mensaje desea enviar");
                                            }
                                        }else{
                                            if (appName.isEmpty()) {
                                                speech.speek("a quien desea enviar mensaje");
                                            }else if (message.isEmpty()) {
                                                message = externalApp.procesarDatosEntrada(matches.get(0).toLowerCase());
                                            }else if(!bConfirm){
                                                speech.speek("Desea enviar el mensaje");
                                                bConfirm = true;
                                            }else{
                                                if (matches.get(0).toLowerCase().equals("si") || matches.get(0).toLowerCase().equals("enviar")){
                                                    whatsapp.SendMessageTo(appName,message);
                                                }
                                            }

                                        }

                                        break;
                                    case "galeria":
                                        gallery.OpenGallery();
                                        break;
                                    case "contacto":
                                        if(matches.get(0).toLowerCase().indexOf("contacto")> -1 ){
                                            contacts.OpenContacts();
                                        }else {
                                            String contactName = contacts.procesarDatosEntrada(matches.get(0).toLowerCase());
                                            String contactNumber = contacts.getContactByName(contactName);
                                            if(matches.get(0).toLowerCase().indexOf("llamar a")> -1){
                                                //Hacer llamado
                                            }else{
                                                //Mandar mensaje
                                            }
                                        }

                                        break;
                                    case "cámara":
                                        Camera.OpenCammera();
                                        break;
                                    case "volumen":
                                        int volume;
                                        matches.set(0,matches.get(0).replace("%","").replace("porcentaje", ""));
                                        if(matches.get(0).toLowerCase().indexOf("multimedia")>0) {
                                       volume =Integer.parseInt(matches.get(0).substring(matches.get(0).toLowerCase().lastIndexOf("volumen multimedia")+22));

                                          sound.setMusicVolumen(volume);
                                        }else{
                                             volume =Integer.parseInt(matches.get(0).substring(matches.get(0).toLowerCase().lastIndexOf("volumen")+11));

                                            sound.setVolumen(volume);
                                        }
                                        speech.speek("El volumen se encuentra en "+ String.valueOf(volume)+ " porciento");
                                        break;
                                     default:
                                         speech.speek("Lo siento, no tengo una respuesta");
                                            break;
                                }


                               // StartvoiceListening();



                            }
                            VoiceRecognition.matches = null;

                        }
                        StartvoiceListening();
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {

                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {

                    }
                };
            }

            voice.setRecognitionListener(rec);
            voice.startListening(intent);
            listening = true;




        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
   public  void InitSpeech() {
        try {
            if(voice == null) {
                voice = SpeechRecognizer.createSpeechRecognizer(MainActivity.getContext());
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
