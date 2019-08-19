package com.android.asistente.asistente.business;

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
}
