package com.example.roshan.quickwriter;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

public class ThemeHelper {
    public static boolean isDarkTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.roshan.quickwriter.FontPreference", Context.MODE_PRIVATE);
        String colorTheme = sharedPreferences.getString("colorTheme", "automatic");
        switch(colorTheme) {
            case "automatic":
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("HH");
                String time = dateformat.format(c.getTime());
                Log.i("NOTESAPP: Time is " , time);
                if(Integer.parseInt(time) >= 18) {
                    return true;
                }
            case "light":
                return false;
            case "dark":
                return true;
        }
        return false;
    }
}
