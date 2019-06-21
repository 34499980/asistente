package com.android.asistente.asistente.Helper;

import android.os.Environment;

import com.android.asistente.asistente.business.Time;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;

public class Log {
   static Time time ;
   static Dictionary date;
    public static void appendLog(String text)
    {
        time = Time.getInstance();
        date = time.getDate();
        File sd = Environment.getExternalStorageDirectory();
        File directory = new File(sd.getAbsolutePath());
        directory.mkdirs();
        File logFile = new File(directory+"/Document/LogAsistente.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(date.get("date")+" "+date.get("hour")+":"+date.get("minutes")+"->"+text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
