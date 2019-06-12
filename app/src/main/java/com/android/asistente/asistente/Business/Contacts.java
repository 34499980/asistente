package com.android.asistente.asistente.Business;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {
    public static ArrayList<Phone> getContactByName(String name){
        String number="";
        Phone phone;
        Boolean bFlag=false;
        name = General.CleanString(name.toLowerCase());
        ArrayList<Phone> listContacs = new ArrayList<Phone>();
       // Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
         //       ContactsContract.CommonDataKinds.Phone.NUMBER};

        //Cursor people = MainActivity.getContext().getContentResolver().query(uri, projection, null, null, null);
        //Prueba contactos
        ContentResolver cr = MainActivity.getContext().getContentResolver();
        Cursor people = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        //Fin prueba
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        //people.moveToFirst();
        people.moveToNext();
        while(people.moveToNext() && !people.getString(indexName).toLowerCase().equals(name)) {
            //Limpio toda la cadea de acentos
            String valueName = General.CleanString(people.getString(indexName).toLowerCase());
            if(valueName.contains(name)){
                if(listContacs.isEmpty()){
                    phone = new Phone();
                    phone.ID = valueName;
                    phone.number = String.valueOf(people.getString(indexNumber));
                    listContacs.add(phone);
                }else {
                    for (int i = 0; i < listContacs.size(); i++) {
                        if (valueName.equals(listContacs.get(i).ID)) {
                          bFlag = true;
                          break;
                        }
                    }
                    if(!bFlag){
                        phone = new Phone();
                        phone.ID = valueName;
                        phone.number = String.valueOf(people.getString(indexNumber));

                        listContacs.add(phone);
                    }
                    bFlag= false;
                }

            }

        }

        return listContacs;


    }
    public  String procesarDatosEntrada(String value){
        String result="";
        int index = value.indexOf("llamar");
        if(index > -1){
            result = value.substring(index+9);
        }else if(value.indexOf("mensaje a") > -1){
            result = value.substring(index+9);

        } if(value.indexOf("abrir contactos") > -1){
            result = "contactos";
        }else{
            result = value;
        }
        return result;
    }
    public  void OpenContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
