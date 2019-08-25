package com.android.asistente.asistente.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Sound extends BroadcastReceiver {
int val;

    //Using volume control UI visibility
//To increase media player volume
    public void setVolumen(int Volumen){
        try {
            val=(Volumen * 15) / 100;
            AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, val, 0);
        }catch (Exception ex){
            Log.appendLog("Sound:"+ex.getMessage());
            throw ex;
        }
    }
    public void setMusicVolumen(int Volumen){
        try {
            val=(Volumen * 15) / 100;
            AudioManager audioManager = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0);
        }catch (Exception ex){
            Log.appendLog("Sound:"+ex.getMessage());
            throw ex;
        }
    }
    public static  int getVolume(){
        try {
            AudioManager audio = (AudioManager) asistenteservice.getContext().getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
            return currentVolume;
        }catch(Exception ex){
            return -1;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = (KeyEvent) intent
                .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_HEADSETHOOK:
                if (action == KeyEvent.ACTION_UP)
                    if (SystemClock.uptimeMillis() - event.getDownTime() > 2000)
                       asistenteservice.startVoice();
                break;
        }
        abortBroadcast();
    }


    private static final String TAG = "MainActivity";


    private static int RECORDER_SAMPLERATE = 44100;
    private static int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;



    static boolean isRecording = false;
   static  private File file;
    private AudioRecord audioRecord;
    static int bufferSizeInBytes = 0;
   static  Context context = asistenteservice.getContext();

    // path
    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Document/final.pcm" ;
    final String outpath = path.replace(".pcm", ".wav");

    public static void autoRecording(){
        // Get the minimum buffer size required for the successful creation of an AudioRecord object.
        bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING
        );
        // Initialize Audio Recorder.
        AudioRecord audioRecorder = new AudioRecord( MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSizeInBytes
        );
        // Start Recording.
        //txv.setText("Ing");
        audioRecorder.startRecording();
        isRecording = true;
Toast.makeText(asistenteservice.getContext(),"Start listening..",Toast.LENGTH_LONG).show();
        // for auto stop
        int numberOfReadBytes   = 0;
        byte audioBuffer[]      = new  byte[bufferSizeInBytes];
        boolean recording       = false;
        float tempFloatBuffer[] = new float[3];
        int tempIndex           = 0;

        // create file

        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/final.pcm");
        //Log.d(TAG, "recording: file path:" + file.toString());

        if (file.exists()){
            //Log.d(TAG,"file exist, delete file");
            file.delete();
        }
        try {
            //Log.d(TAG,"file created");
            file.createNewFile();
        } catch (IOException e) {
            //Log.d(TAG,"didn't create the file:" + e.getMessage());
            throw new IllegalStateException("did not create file:" + file.toString());
        }

        // initiate media scan and put the new things into the path array to
        // make the scanner aware of the location and the files you want to see
        MediaScannerConnection.scanFile(asistenteservice.getContext(), new String[] {file.toString()}, null, null);

        // output stream
        OutputStream os = null;
        DataOutputStream dos = null;
        try {
            os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            dos = new DataOutputStream(bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // While data come from microphone.
        while( true )
        {
            float totalAbsValue = 0.0f;
            short sample        = 0;

            numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );

            // Analyze Sound.
            for( int i=0; i<bufferSizeInBytes; i+=2 )
            {
                sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
                totalAbsValue += (float)Math.abs( sample ) / ((float)numberOfReadBytes/(float)2);
            }

            // read in file
          /*  for (int i = 0; i < numberOfReadBytes; i++) {
                try {
                    dos.writeByte(audioBuffer[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            // Analyze temp buffer.
            tempFloatBuffer[tempIndex%3] = totalAbsValue;
            float temp                   = 0.0f;
            for( int i=0; i<3; ++i ) {
                temp += tempFloatBuffer[i];
                System.out.println(tempFloatBuffer[i]);
            }
            if( (temp >=0 && temp <= 2100) && recording == false )  // the best number for close to device: 3000
            {                                                       // the best number for a little bit distance : 2100
            //    Log.i("TAG", "1");
                tempIndex++;
                continue;
            }

            if( temp > 2100 && recording == false )
            {
          //      Log.i("TAG", "2");
                recording = true;
            }

            if( (temp >= 0 && temp <= 2100) && recording == true )
            {

                //Log.i("TAG", "final run");
                //isRecording = false;

              //  txv.setText("Stop Record.");
                //*/
                tempIndex++;
                audioRecorder.stop();
                try {
                    dos.close();
                    Toast.makeText(asistenteservice.getContext(),"Stop listening..",Toast.LENGTH_LONG).show();
                    Sound sound = new Sound();
                    sound.setMusicVolumen(0);
                            VoiceRecognition voice = new VoiceRecognition();
                            voice.InitSpeech();
                            voice.StartvoiceListening();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
