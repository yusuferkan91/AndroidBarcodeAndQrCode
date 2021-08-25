package me.dm7.golive.sayac.sample;

import android.widget.TextView;

public class logSave {
    public static String txt;
    public static TextView textview;


    public static void logAdd(String text){
        if(txt == null){
            txt = text + "\n";
        }
        else{
            txt = txt + text + "\n";
        }
        //textview.setText(txt);
    }
}
