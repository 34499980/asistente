package com.android.asistente.asistente.business;

import com.android.asistente.asistente.Helper.Log;

import java.time.Year;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;

public class Time {
    private static Time instance = null;
    private Time(){};
    static Dictionary result = new Hashtable();
    public static Time getInstance(){
        if (instance == null){
            instance = new Time();
        }
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
        result.put("date",Day+"/"+Month+"/"+ Year);
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
}
