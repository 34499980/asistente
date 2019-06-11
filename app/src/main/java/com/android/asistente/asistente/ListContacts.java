package com.android.asistente.asistente;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.ImageAdapter;

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
        List<Phone> list =  General.list;
        if(list != null) {
            grid = (GridView) findViewById(R.id.grdContacts);
            ImageAdapter adapter = new ImageAdapter(this,null,  list);
            try {
                grid.setAdapter(adapter);
            }catch(Exception ex){
                Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

    }
}
