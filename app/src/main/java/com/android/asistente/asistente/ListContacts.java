package com.android.asistente.asistente;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.ImageAdapter;
import com.android.asistente.asistente.business.Call;
import com.android.asistente.asistente.business.VoiceRecognition;

import java.io.Serializable;
import java.util.List;

public class ListContacts extends Activity implements Serializable {
public static Context context = null;
GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_list_contacts);
        }catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        context = getApplicationContext();
       final List<Phone> list =  General.list;
        if(list != null) {
            grid = (GridView) findViewById(R.id.grdContacts);
            ImageAdapter adapter = new ImageAdapter(this,null,  list);
            try {
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        General.selectedPhone = list.get(i);
                        if(VoiceRecognition.letters.equals("whatsapp")){
                            VoiceRecognition.appName =  General.selectedPhone.ID;
                            finish();

                        }else{
                            Call call = new Call();
                            call.startCall(General.selectedPhone.number);
                        }

                    }
                });
            }catch(Exception ex){
                Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

    }
}
