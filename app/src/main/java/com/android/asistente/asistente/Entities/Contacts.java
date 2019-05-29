package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.android.asistente.asistente.MainActivity;

public class Contacts extends AppCompatActivity {
    public static String getContactByName(String name){
        String number="";
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = MainActivity.getContext().getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        //people.moveToFirst();
        people.moveToNext();
        while(people.moveToNext() && !people.getString(indexName).toLowerCase().equals(name)) {
            String valueName = people.getString(indexName).toLowerCase();
            if(valueName.contains(name)){
                return people.getString(indexNumber);
            }

        }

        return number;


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
        }
        return result;
    }
    public  void OpenContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
