package com.android.asistente.asistente.business;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.Entities.Weather;
import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.ListContacts;

import com.android.asistente.asistente.MainActivity;
import com.android.asistente.asistente.R;
import com.android.asistente.asistente.Services.TTSService;
import com.android.asistente.asistente.Services.asistenteJobService;
import com.android.asistente.asistente.Services.asistenteservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoiceRecognition extends AppCompatActivity implements Serializable {
    private static final int REQ_CODE_SPEECH_INPUT = 1000;
    Call call = new Call();
    private SpeechRecognizer voice;
    static ArrayList<String> matches;
    RecognitionListener rec;
    Intent intent;
    Button btnPrueba;
    Whatsapp whatsapp = new Whatsapp();
    ExternalApp externalApp = new ExternalApp();
    Contacts contacts = new Contacts();
    int countSearch = 0;
    public static boolean  bFlag= false;
    boolean bConfirm = false;
    public static boolean bSelectContac = false;
    //final Speech speech = new Speech();
    Sound sound = new Sound();
    public static String appName = "";
    String message;
    List<Phone> listContacts= null;
    View mView;
    public static String letters=null;
    public static boolean listening;
    asistenteservice asis=null;
    private Boolean continuos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            btnPrueba = (Button) findViewById(R.id.btnHablar);
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }




    }

    public void StartvoiceListening(){
       // Log.appendLog("StartvoiceListening inicio");
        listening = true;
      //  sound.setMusicVolumen(0);

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
                       // sound.setMusicVolumen(70);
                        if(matches == null){
                            StartvoiceListening();
                        }
                    }

                    @Override
                    public void onError(int i) {
                   //     sound.setMusicVolumen(70);
                        try{

                        }catch(Exception ex){

                        }
                  //   Toast.makeText(MainActivity.getContext(),"Error al intentar escuchar. Posiblemente no tenga conexión!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResults(Bundle data) {
                    //    Log.appendLog("onResults inicio");
                      //  sound.setMusicVolumen(70);
                        matches = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        listening=false;
                        if (matches != null) {

                            if (matches.get(0).toLowerCase().equals("hola")) {
                                    TTSService.speak("En que lo puedo ayudar");
                            } else if(matches.get(0).toLowerCase().indexOf("cancelar acción") > -1){
                                CancelAction();
                            }else if(matches.get(0).toLowerCase().indexOf("desactivar servicio") > -1) {
                                MainActivity.bActive = false;
                                asistenteservice.bActive = false;
                                if( MainActivity.btnStopService != null){
                                    MainActivity.btnStopService.performClick();
                                }

                            }else{
                                //Si no hablo, busca accion. Sino continua el proceso anterior.
                                if(!bFlag) {
                                    //Procesa la accion de entrada.
                                   Input();
                                }

                                //Ejecuta los comandos
                                ExecuteCommand();
                            }
                            VoiceRecognition.matches = null;

                        }
                      //  Log.appendLog("onResults Fin");
                        if(asistenteservice.bActive && continuos) {
                            StartvoiceListening();
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {
                        listening = false;
                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {

                    }
                };
            }

            voice.setRecognitionListener(rec);

                    voice.startListening(intent);

            listening = true;

         //   Log.appendLog("StartvoiceListening Fin");

        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ExecuteCommand(){
        try {
            if (letters != null) {
                switch (letters) {
                    case "tiempo":
                        try {
                            if (matches.get(0).toLowerCase().indexOf("hora") > -1) {
                                TTSService.speak(Time.getHoursAndMinutes());
                            } else if (matches.get(0).toLowerCase().indexOf("temperatura") > -1) {

                                Time time = Time.getInstance();
                                time.getTemperatureNow("temperatura");

                                //  TTSService.speak("Hay "+ String.valueOf(weather.temperature) + " grados " +weather.sky);
                            } else if (matches.get(0).toLowerCase().indexOf("clima") > -1) {
                                Time time = Time.getInstance();
                                time.getTemperatureNow("clima");
                           /* TTSService.speak("Hay "+ String.valueOf(weather.temperature) + " grados. Se espera "+
                                   weather.min+ " de mínima y "+ weather.max + "de máximo, con una humedad de "+
                                    weather.humidity +" porciento "  +weather.sky);*/
                            }
                        }catch(Exception ex){
                            Log.appendLog(ex.getMessage());
                            Toast.makeText(this,"No se pudo obtener el clima",Toast.LENGTH_LONG).show();
                        }
                        CancelAction();
                        break;
                    case "ExternalApp":
                        appName = externalApp.procesarDatosEntrada(matches.get(0).toLowerCase());
                        ResolveInfo app = externalApp.getAllAplication(appName);
                        externalApp.startApp(app);
                        CancelAction();
                        break;
                    case "whatsapp":
                        continuos = true;
                        if (!bFlag) {
                            if (appName.isEmpty()) {
                                appName = whatsapp.ProcesarDatosEntrada(matches.get(0).toLowerCase());
                            }
                            if (!appName.isEmpty()) {
                                listContacts = Contacts.getContactByName(appName);
                            }
                            if (listContacts.isEmpty()) {
                                if (countSearch < 3) {
                                    TTSService.speak("a quien desea enviar mensaje");
                                    appName = "";
                                    countSearch++;
                                } else {
                                    TTSService.speak("No se pudo encontrar el contacto. Busquelo manualmente.");
                                    CancelAction();
                                }
                            } else if (listContacts.size() > 1) {

                                try {

                                    TTSService.speak("Seleccione uno de los contactos");
                                    if (!listContacts.isEmpty()) {
                                        Intent t = new Intent(asistenteservice.getContext(), ListContacts.class);
                                        General.list = listContacts;
                                        asistenteservice.getContext().startActivity(t);
                                    }
                                } catch (Exception ex) {
                                    Log.appendLog(ex.getMessage());
                                    Toast.makeText(asistenteservice.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                appName = ((Phone) listContacts.get(0)).number;
                                bFlag = true;
                                TTSService.speak("que mensaje desea enviar");
                            }
                        } else {

                            if (message == null && !bConfirm) {
                                message = matches.get(0);
                                TTSService.speak("Desea enviar el mensaje");
                            } else {
                                if (matches.get(0).toLowerCase().equals("sí") || matches.get(0).toLowerCase().equals("enviar")) {
                                    whatsapp.SendMessageTo(appName, message);
                                    message = "";
                                    appName = "";
                                    bFlag = false;
                                    bConfirm = false;
                                    CancelAction();
                                }
                            }

                        }

                        break;
                    case "contacto":
                        if (matches.get(0).toLowerCase().indexOf("contacto") > -1) {
                            contacts.OpenContacts();
                        } else {
                            String contactName = contacts.procesarDatosEntrada(matches.get(0).toLowerCase());
                            listContacts = contacts.getContactByName(contactName);
                            if (listContacts.size() == 1) {
                                if (matches.get(0).toLowerCase().indexOf("llamar a") > -1) {
                                    call.startCall(((Phone) listContacts.get(0)).number);
                                    CancelAction();
                                } else {
                                    //Mandar mensaje
                                }
                            } else {
                                try {
                                    bFlag = true;
                                    bSelectContac = true;
                                    continuos = true;
                                    TTSService.speak("Seleccione uno de los contactos");
                                    if (!listContacts.isEmpty()) {
                                        Intent t = new Intent(asistenteservice.getContext(), ListContacts.class);
                                        General.list = listContacts;
                                        asistenteservice.getContext().startActivity(t);
                                    }
                                } catch (Exception ex) {
                                    Log.appendLog(ex.getMessage());
                                    CancelAction();
                                    Toast.makeText(asistenteservice.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        break;
                    case "cámara":
                        Camera.OpenCammera();
                        CancelAction();
                        break;
                    case "volumen":
                        int volume;
                        matches.set(0, matches.get(0).replace("%", "").replace("porcentaje", ""));
                        if (matches.get(0).toLowerCase().indexOf("multimedia") > 0) {
                            volume = Integer.parseInt(matches.get(0).substring(matches.get(0).toLowerCase().lastIndexOf("volumen multimedia") + 22));

                            sound.setMusicVolumen(volume);
                        } else {
                            volume = Integer.parseInt(matches.get(0).substring(matches.get(0).toLowerCase().lastIndexOf("volumen") + 11));

                            sound.setVolumen(volume);
                        }
                        TTSService.speak("El volumen se encuentra en " + String.valueOf(volume) + " porciento");
                        CancelAction();
                        break;
                    default:
                        TTSService.speak("Lo siento, no tengo una respuesta");
                        break;
                }
            } else {
                TTSService.speak("Lo siento, no tengo una respuesta");
            }
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
    }
    private void Input(){
        try {
            if (matches.get(0).toLowerCase().indexOf("volumen") > -1) {
                letters = "volumen";
            } else if (matches.get(0).toLowerCase().indexOf("qué hora es") > -1 ||  matches.get(0).toLowerCase().indexOf("temperatura") > -1||  matches.get(0).toLowerCase().indexOf("clima") > -1){
                letters = "tiempo";
            } else if (matches.get(0).toLowerCase().contains("enviar whatsapp") || matches.get(0).toLowerCase().contains("enviar un whatsapp")) {
                letters = "whatsapp";
            } else if (matches.get(0).toLowerCase().indexOf("llamar") > -1 || matches.get(0).toLowerCase().indexOf("mensaje") > -1) {
                letters = "contacto";
            } else if (matches.get(0).toLowerCase().indexOf("abrir") > -1) {
                letters = "ExternalApp";
            }
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
        }
    }
    private void CancelAction(){
        bConfirm = false;
        bFlag = false;
        bSelectContac= false;
        appName = "";
        letters = "";
        countSearch = 0;
        continuos = false;
    }
    public  void InitSpeech() {
        try {

          // Log.appendLog("InitSpeech inicio");

            if(voice == null) {
               General general = new General();
                       general.startService(asistenteservice.class);


                asis =  new asistenteservice();
                voice = SpeechRecognizer.createSpeechRecognizer(asistenteservice.getContext());
            }
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    asistenteservice.getContext().getPackageName());
       //     Log.appendLog("InitSpeech Fin");

        }catch(Exception ex){
            Log.appendLog(ex.getMessage());
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
