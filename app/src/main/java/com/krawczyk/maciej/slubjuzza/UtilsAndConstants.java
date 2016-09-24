package com.krawczyk.maciej.slubjuzza;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class UtilsAndConstants {

    private final static int MILLIS_IN_SECOND = 1000;
    private final static int SECONDS_IN_MINUTE = 60;
    private final static int MINUTES_IN_HOUR = 60;
    private final static int HOURS_IN_DAY = 24;
    private final static int DAYS_IN_YEAR = 365;

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String BROADCAST_NOTIFICATION_FROM_ALARM = "com.krawczyk.maciej.slubjuzza.alarm";

    public static final String WEDDING_YEAR = "WeddingYear";
    public static final String WEDDING_MONTH = "WeddingMonth";
    public static final String WEDDING_DAY = "WeddingDay";
    public static final String WEDDING_HOUR = "WeddingHour";
    public static final String WEDDING_MINUTES = "WeddingMinutes";
    public static final String ONE_DAY_PATTERN = "365";

    public final static long MILLISECONDS_IN_HOUR = (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
    public final static long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * HOURS_IN_DAY;
    public final static long MILLISECONDS_IN_YEAR = MILLISECONDS_IN_DAY * DAYS_IN_YEAR;


    private Context context;

    public UtilsAndConstants(Context context) {
        this.context = context;
    }

    public Calendar getTimeFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        Calendar calendarSavedTime = Calendar.getInstance();
        int year = sharedPreferences.getInt(WEDDING_YEAR, 0);
        int month = sharedPreferences.getInt(WEDDING_MONTH, 0);
        int day = sharedPreferences.getInt(WEDDING_DAY, 0);
        int hour = sharedPreferences.getInt(WEDDING_HOUR, 0);
        int minute = sharedPreferences.getInt(WEDDING_MINUTES, 0);

        if (year != 0) {
            calendarSavedTime.set(Calendar.YEAR, year);
            calendarSavedTime.set(Calendar.MONTH, month);
            calendarSavedTime.set(Calendar.DAY_OF_MONTH, day);
        }
        if (hour != 0) {
            calendarSavedTime.set(Calendar.HOUR_OF_DAY, hour);
            calendarSavedTime.set(Calendar.MINUTE, minute);
        }
        return calendarSavedTime;
    }

}
