package com.krawczyk.maciej.slubjuzza;

import android.content.Context;
import android.content.SharedPreferences;

public class AlarmSettings {

    private static final String MyPreferences = "MyPrefs" ;
    private static final String WEDDING_ALARM_FLAG = "WeddingAlarmFlag";

    public static void setWeatherAlarmUnderTemp(Context context, Boolean useWeatherAlarm){
        SharedPreferences settings = context.getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(WEDDING_ALARM_FLAG, useWeatherAlarm).apply();
    }

    public static boolean isTimeAlarmSet(Context context){
        SharedPreferences settings = context.getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        return settings.getBoolean(WEDDING_ALARM_FLAG, false);
    }
}