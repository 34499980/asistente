package com.android.asistente.asistente.Entities;

public class Weather {
    static Weather instance = null;
    public static int temperature = -999;
    public static String sky;
    public static String pressure;
    public static String humidity;
    public static int min;
    public static int max;

    private Weather(){

    }
    public static Weather getInstance(){
        if(instance == null){
            instance = new Weather();
        }
        return instance;

    }

}
