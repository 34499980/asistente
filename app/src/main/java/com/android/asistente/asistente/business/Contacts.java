package com.android.asistente.asistente.business;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.asistenteservice;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    public static ArrayList<Phone> getContactByName(String name){
        try {
            String number = "";
            Phone phone;
            Boolean bFlag = false;
            name = General.CleanString(name.toLowerCase());
            ArrayList<Phone> listContacs = new ArrayList<Phone>();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor people = asistenteservice.getContext().getContentResolver().query(uri, projection, null, null, null);
            //Prueba contactos
            //ContentResolver cr = MainActivity.getContext().getContentResolver();
            //Cursor people = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
            //       null, null, null);
            //Fin prueba
            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            //people.moveToFirst();
            people.moveToNext();
            while (people.moveToNext() && !General.CleanString(people.getString(indexName)).toLowerCase().equals(name)) {
                //Limpio toda la cadea de acentos
                String valueName = General.CleanString(people.getString(indexName).toLowerCase());
                if (valueName.contains(name)) {
                    if (listContacs.isEmpty()) {
                        phone = new Phone();
                        phone.ID = valueName;
                        phone.number = String.valueOf(people.getString(indexNumber));
                        listContacs.add(phone);
                    } else {
                        for (int i = 0; i < listContacs.size(); i++) {
                            if (valueName.equals(listContacs.get(i).ID)) {
                                bFlag = true;
                                break;
                            }
                        }
                        if (!bFlag) {
                            phone = new Phone();
                            phone.ID = valueName;
                            phone.number = String.valueOf(people.getString(indexNumber));

                            listContacs.add(phone);
                        }
                        bFlag = false;
                    }

                }

            }

            return listContacs;
        }catch(Exception ex){
            Log.appendLog(Contacts.class.getName()+"->"+ Contacts.class.getEnclosingMethod().getName());
            throw ex;
        }

    }
    public  String procesarDatosEntrada(String value){
        try {
            String result = "";
            int index = value.indexOf("llamar");
            if (index > -1) {
                result = value.substring(index + 9);
            } else if (value.indexOf("mensaje a") > -1) {
                result = value.substring(index + 9);


            } else {
                result = value;
            }
            return result;
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            throw ex;
        }
    }
    public  void OpenContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
