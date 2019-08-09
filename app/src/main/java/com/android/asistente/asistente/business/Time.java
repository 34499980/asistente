package com.android.asistente.asistente.business;

import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.android.asistente.asistente.Entities.Weather;
import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

public class Time {
    private static Time instance = null;
    private static String OPEN_WEATHER_MAP_API = "4deb0b5d37a470b706c934ee7471c7a8";
    public static int temperature = -999;
    public static String sky;
    private Time(){};
    private static Weather weather;
    String _input = null;

    static Dictionary result = new Hashtable();
    public static Time getInstance(){
        if (instance == null){
            instance = new Time();
        }
        weather = Weather.getInstance();
        return  instance;
    }

    public static Dictionary<String,String> getDate(){
    try{
        Calendar cal = Calendar.getInstance();
        String Hour = String.valueOf(cal.get(Calendar.HOUR));
        String Minutes = String.valueOf(cal.get(Calendar.MINUTE));
        String Day = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
        String Month = String.valueOf(cal.get(Calendar.MONTH)+1);
        String Year = String.valueOf(cal.get(Calendar.YEAR));
        result.put("date",new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        result.put("day",Day);
        result.put("month",Month);
        result.put("year",Year);
        result.put("hour",Hour);
        result.put("minutes",Minutes);
        return result;
        }catch(Exception ex){
        Log.appendLog(Time.class.getName()+"->"+ Time.class.getEnclosingMethod().getName());
        throw ex;
         }
    }
    public static String getHoursAndMinutes(){
        getDate();
      return "Son las "+ result.get("hour") + " y "+ result.get("minutes");
    }

    public  void getTemperatureNow(String input){
        try {
            _input = input;
            DownloadWeather weather = new DownloadWeather();
            weather.execute("Buenos Aires,AR");
        }catch(Exception ex){
            Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
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
        } catch (Exception e) {
            Log.appendLog(Time.class.getName()+"->"+ Time.class.getEnclosingMethod().getName());
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
    class DownloadWeather extends AsyncTask< String, Void, String > {
        DownloadWeather instance=null;
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
            String xml = Time.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    // cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    switch (details.getString("description").toLowerCase()){
                        case "clear sky":
                            weather.sky = "y el cielo se encuentra despejado";
                            break;
                        case "few clouds":
                            weather.sky = "y el cielo se encuentra nublado";
                            break;
                        case "scattered clouds":
                            weather.sky = "y el cielo se encuentra algo nublado";
                            break;
                        case "broken clouds":
                            weather.sky = "y el cielo se encuentra algo nublado";
                            break;
                        case "shower rain":
                            weather.sky = "y esta lloviendo";
                            break;
                        case "rain":
                            weather.sky = "y esta lloviendo";
                            break;
                        case "thunderstorm":
                            weather.sky = "y hay tormenta electrica";
                            break;
                        case "snow":
                            weather.sky = "y esta nevando";
                            break;
                        case "moderate rain":
                            weather.sky = "y hay llovizna";
                            break;
                        case "mist":
                            weather.sky = "y hay niebla";
                            break;
                        case "light intensity drizzle":
                            weather.sky = "y hay llovizna";
                            break;
                        case "drizzle":
                            weather.sky = "y hay llovizna";
                            break;
                        case "light rain":
                            weather.sky = "y hay llovizna";
                            break;
                        default:
                            weather.sky="";
                            break;
                    }
                    weather.temperature = Integer.parseInt( main.getString("temp").substring(0,main.getString("temp").indexOf(".")));
                    weather.min = Math.round(main.getLong("temp_min"));//.substring(0,main.getString("temp_min").indexOf(".")));

                    weather.max = Math.round( main.getLong("temp_max"));//.substring(0,main.getString("temp_max").indexOf(".")));

                    weather.pressure = main.getString("pressure").toLowerCase();
                    weather.humidity = main.getString("humidity").toLowerCase();
                    // humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    // pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    // updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    //weatherIcon.setText(Html.fromHtml(Time.setWeatherIcon(details.getInt("id"),
                    //  json.getJSONObject("sys").getLong("sunrise") * 1000,
                    //   json.getJSONObject("sys").getLong("sunset") * 1000)));

                    //  loader.setVisibility(View.GONE);
                    if(_input.equals("temperatura")){
                        TTSService.speak("Hay "+ String.valueOf(weather.temperature) + " grados " +weather.sky);
                    }else{
                        TTSService.speak("Hay "+ String.valueOf(weather.temperature) + " grados. Se espera "+
                                weather.min+ " de mínima y "+ weather.max + "de máximo, con una humedad de "+
                                weather.humidity +" porciento "  +weather.sky);
                    }

                }
            } catch (JSONException e) {
                Log.appendLog(getClass().getName()+"->"+getClass().getEnclosingMethod().getName());
            }


        }



    }
}
