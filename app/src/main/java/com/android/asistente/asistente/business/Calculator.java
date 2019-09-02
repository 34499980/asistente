package com.android.asistente.asistente.business;

import com.android.asistente.asistente.Helper.Log;
import com.android.asistente.asistente.Services.TTSService;

public class Calculator {

    public static String Calculate(){
        String result="";


        return result;
    }


    public static String ProcesarDatosEntrada(String value){
        String result = "";
        String inputString;
        float num1 = 0;
        float num2 = 0;
        int index;
        try {

            value = value.toLowerCase().replace("cuanto es","").replace("calcular","").replace("más","+")
            .replace("menos","-").replace("por","*").replace("dividido","/").replace("uno","1")
            .replace("dos","2").replace(",",".");
            if(value.toLowerCase().contains("+")){
                index = value.indexOf("+");
                num1 = Float.parseFloat(value.substring(0,index).trim());
                num2 = Float.parseFloat(value.substring(index+1).trim());
                result = String.valueOf(num1 + num2);

            }else if(value.toLowerCase().contains("-")){
                index = value.indexOf("-");
                num1 = Float.parseFloat(value.substring(0,index).trim());
                num2 = Float.parseFloat(value.substring(index+1).trim());
                result = String.valueOf(num1 - num2);
            }else if(value.toLowerCase().contains("*")) {
                index = value.indexOf("*");
                num1 = Float.parseFloat(value.substring(0,index).trim());
                num2 = Float.parseFloat(value.substring(index+1).trim());
                result = String.valueOf(num1 * num2);
            }else{
                index = value.indexOf("/");
                num1 = Float.parseFloat(value.substring(0,index).trim());
                num2 = Float.parseFloat(value.substring(index+1).trim());
                result = String.valueOf(num1 / num2);
            }
            return result.contains(".0") ? result.replace(".0",""): result;
        }catch(Exception ex){
            Log.appendLog("Calculator: "+ex.getMessage());
            TTSService.speak("No pude realizar el calculo.");
            return result;
        }
    }
}
