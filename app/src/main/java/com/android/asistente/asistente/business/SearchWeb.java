package com.android.asistente.asistente.business;

import android.os.AsyncTask;

import com.android.asistente.asistente.Helper.General;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class SearchWeb {
        public String query="";
        public static String url;

    public class HttpRequest {

        public  synchronized String doRequest(String urlRequest) throws Exception
        {
            String jsonRespuesta = "";

            HttpURLConnection urlConnection= null;
            URL url = new URL(urlRequest);
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            jsonRespuesta = convertStreamToString(urlConnection.getInputStream());

            return jsonRespuesta;
        }

        private  String convertStreamToString(InputStream is) throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            return sb.toString();
        }

    }
    public class UrlFactory {

        private static final String url = "https://www.googleapis.com/customsearch/v1?";
        private static final  String KEY = "AIzaSyD0xPBxooSwcSxb2J16vPJE7HQtvxjqyPk";
        private static final String cx = "016697470152593144145:catp7rewrfa";

        public  String buildUrl(String keywords) throws Exception{
            return url +"?key=" +KEY+ "&cx=" + cx + "&q=" + URLEncoder.encode(keywords);
        }

    }
    public class ResultDeserializer {

       /* public void deserialize(String object) throws JSONException {
            //Result result = new Result();

            final List<Item> items = new ArrayList<Item>();

            JSONObject jSonObject = new JSONObject(object);
            JSONArray jSonObjectArray = jSonObject.getJSONArray("items");
            for (int count = 0; count < jSonObjectArray.length(); count++) {
                JSONObject jsonItem = (JSONObject) jSonObjectArray.get(count);

                Item item = new Item();
                item.setTitle(jsonItem.getString("title"));
                item.setHtmlTitle(jsonItem.getString("htmlTitle"));
                item.setLink(jsonItem.getString("link"));
                item.setDisplayLink(jsonItem.getString("displayLink"));
                item.setSnippet(jsonItem.getString("snippet"));
                item.setHtmlSnippet(jsonItem.getString("htmlSnippet"));

                items.add(item);
            }

            result.setItems(items);

            return result;
        }*/
    }
    public  void Searh(String input){
        try {

            DownloadWeather weather = new DownloadWeather();
            weather.execute(input);
        }catch(Exception ex){
            Log.appendLog("SearchWeb:"+ex.getMessage());
        }
    }
    private static  String excuteGet(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception ex) {
            Log.appendLog("SearchWeb:"+ex.getMessage());
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
    class DownloadWeather extends AsyncTask< String, Void, String > {
        Time.DownloadWeather instance=null;
        /* private DownloadWeather(){};
         public DownloadWeather getInstance(){
             if(instance == null){
                 instance = new DownloadWeather();
             }
             return instance;
         }*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
             String url = "https://www.googleapis.com/customsearch/v1?";
             String KEY = "AIzaSyD0xPBxooSwcSxb2J16vPJE7HQtvxjqyPk";
             String cx = "016697470152593144145:catp7rewrfa";
            String buildUrl;
            if(args[0].toLowerCase().contains("buscar")) {
                buildUrl = "https://www.googleapis.com/customsearch/v1?key=AIzaSyD0xPBxooSwcSxb2J16vPJE7HQtvxjqyPk&cx=016697470152593144145:catp7rewrfa&q=" + args[0];
            }else{
                buildUrl = "https://www.googleapis.com/customsearch/v1?key=AIzaSyD0xPBxooSwcSxb2J16vPJE7HQtvxjqyPk&cx=016697470152593144145:catp7rewrfa&q=" + args[0] + "Wikipedia, enciclopedia libre";

            }
             String xml = excuteGet(buildUrl);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("items").getJSONObject(0);
                   // JSONObject main = json.getJSONObject("main");
                   // DateFormat df = DateFormat.getDateTimeInstance();
                    url = details.getString("formattedUrl");
                    TTSService.speak(General.CleanStringFromHTML(details.getString("htmlSnippet").toLowerCase()));
                    // cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    VoiceRecognition.CancelAction();
                }
            } catch (JSONException ex) {
                Log.appendLog("SearchWeb:"+ex.getMessage());
            }


        }



    }
}
