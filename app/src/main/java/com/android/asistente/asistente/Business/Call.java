package com.android.asistente.asistente.Business;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.asistente.asistente.MainActivity;

public class Call extends AppCompatActivity {
    public void startCall(String number){
        try {
            if(number!="") {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: +549" + number));
                MainActivity.getContext().startActivity(callIntent);
            }
        }catch(Exception ex){
            Toast.makeText(this, "No se pudo realizar la llamada", Toast.LENGTH_SHORT).show();
        }
    }
}
