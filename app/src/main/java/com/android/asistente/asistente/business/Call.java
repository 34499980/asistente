package com.android.asistente.asistente.business;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

public class Call extends AppCompatActivity {
    public void startCall(String number){
        try {
            if(number!="") {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: +549" + number));
                asistenteservice.getContext().startActivity(callIntent);
            }
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            Toast.makeText(this, "No se pudo realizar la llamada", Toast.LENGTH_SHORT).show();
        }
    }
}
