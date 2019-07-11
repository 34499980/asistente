package com.android.asistente.asistente.business;

import com.android.asistente.asistente.Helper.Log;
import com.loopj.android.http.HttpGet;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class SearchWeb {
    public void search(String query){
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("https://google.com/search?q="+query));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                Header[] headers = response.getAllHeaders();
                for (Header item: headers) {
                    item.getValue();
                    item.getElements();
                    item.getName();

                }
                //Here I do something with the Date String
                //System.out.println(dateStr);

            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }catch (ClientProtocolException e) {
            Log.appendLog( e.getMessage());
        }catch (IOException e) {
            Log.appendLog(e.getMessage());
        }
    }
}
