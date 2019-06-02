package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.android.asistente.asistente.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {
    public static List<Cursor> getContactByName(String name){
        String number="";
        List<Cursor> listContacs = new ArrayList<Cursor>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = MainActivity.getContext().getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        //people.moveToFirst();
        people.moveToNext();
        while(people.moveToNext() && !people.getString(indexName).toLowerCase().equals(name)) {
            String[] peopleCell = people.getString(indexName).toLowerCase().split(" ");
            String[] inputName = name.split(" ");
            int index=0;
            int words=inputName[0].length();
            int total = inputName.length;
            int porcent = 100/words;
            int porcentTotal = 0;
            int i= 0;
            while(index <= total){
                String value = peopleCell[index];
                String input = inputName[0];

                while(i <= words){
                    if(input.charAt(i)==value.charAt(i)){
                        porcentTotal+=porcent;
                    }
                }
                if(porcent > 75) {
                    listContacs.add(people);
                }
                index++;
            }

               // return people.getString(indexNumber);


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
        }
        return result;
    }
    public  void OpenContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
