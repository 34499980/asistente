package com.android.asistente.asistente.Entities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Gallery extends AppCompatActivity {
    public void OpenGallery(){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
