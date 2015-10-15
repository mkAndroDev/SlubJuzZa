package com.krawczyk.maciej.slubjuzza.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.krawczyk.maciej.slubjuzza.AlarmSettings;
import com.krawczyk.maciej.slubjuzza.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String MyPreferences = "MyPrefs" ;
    public static final String BROADCAST_NOTIFICATION_FROM_ALARM = "com.krawczyk.maciej.slubjuzza.alarm";

    private static final String WeddingYear = "WeddingYear" ;
    private static final String WeddingMonth = "WeddingMonth" ;
    private static final String WeddingDay = "WeddingDay" ;
    private static final String WeddingHour = "WeddingHour" ;
    private static final String WeddingMinutes = "WeddingMinutes" ;
    private static final String OneDayPattern = "365";

    private final static int MILLIS_IN_SECOND = 1000;
    private final static int SECONDS_IN_MINUTE = 60;
    private final static int MINUTES_IN_HOUR = 60;
    private final static int HOURS_IN_DAY = 24;
    private final static int DAYS_IN_YEAR = 365;
    private final static long MILLISECONDS_IN_HOUR = (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
    private final static long MILLISECONDS_IN_DAY = (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;
    private final static long MILLISECONDS_IN_YEAR = (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR;

    private final SimpleDateFormat simpleDateFormatDays = new SimpleDateFormat("DD");
    private final SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("HH:mm:ss");

    private int mYear, mMonth, mDay, mHour, mMinute;

    private Chronometer chronometerToWedding;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewToWedding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Calendar calendarSetTime;
    private Calendar calendarSetTimeInGoodFormat;

    private void setWeatherAlarm(){
        AlarmSettings.setWeatherAlarmUnderTemp(this, true);

        Intent intent = new Intent(BROADCAST_NOTIFICATION_FROM_ALARM);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (calendarSetTime != null){
            AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTimeInMillis(System.currentTimeMillis());

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(),
                    10000, alarmIntent);
        }
    }

    private void cancelAlarm(){
        AlarmSettings.setWeatherAlarmUnderTemp(this, false);
        Intent intent = new Intent(BROADCAST_NOTIFICATION_FROM_ALARM);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.cancel(alarmIntent);
    }

    public static String getYearsToWedding(Calendar setDate) {
        Calendar currentDate = Calendar.getInstance();
        long yearToWeddingInMillis = setDate.getTimeInMillis() - currentDate.getTimeInMillis();
        long yearToWedding = setDate.get(Calendar.YEAR) - currentDate.get(Calendar.YEAR);

        Calendar test = Calendar.getInstance();
        test.setTimeInMillis(yearToWedding);
        String yearsToWedding = "";

        if (yearToWedding == 0){
            yearsToWedding = "";
        } else if (yearToWedding == 1 && yearToWeddingInMillis > MILLISECONDS_IN_YEAR){
            yearsToWedding = yearToWedding + " rok ";
        } else if (yearToWedding >= 2 && yearToWedding <= 4){
            yearsToWedding = yearToWedding + " lata ";
        } else if (yearToWedding >= 5){
            yearsToWedding = yearToWedding + " lat ";
        }
        return yearsToWedding;
    }

    private void startChronometer(){
        takeTimeFromSP();

        Calendar currentDate = Calendar.getInstance();

        if (calendarSetTime != null){
            if (calendarSetTime.getTimeInMillis() > currentDate.getTimeInMillis()){
                chronometerToWedding.setVisibility(View.VISIBLE);
                textViewToWedding.setVisibility(View.VISIBLE);

                calendarSetTimeInGoodFormat = Calendar.getInstance();
                calendarSetTimeInGoodFormat.setTimeInMillis(calendarSetTime.getTimeInMillis() - MILLISECONDS_IN_HOUR - MILLISECONDS_IN_DAY);

                chronometerToWedding.setBase(calendarSetTimeInGoodFormat.getTimeInMillis());
                chronometerToWedding.start();

                chronometerToWedding.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        Calendar currentDate = Calendar.getInstance();
                        long toWedding = calendarSetTimeInGoodFormat.getTimeInMillis() - currentDate.getTimeInMillis();

                        if (simpleDateFormatDays.format(toWedding).equals(OneDayPattern)) {
                            chronometer.setText(getYearsToWedding(calendarSetTimeInGoodFormat) + "\n" + simpleDateFormatHours.format(toWedding));
                        } else {
                            chronometer.setText(getYearsToWedding(calendarSetTimeInGoodFormat) + simpleDateFormatDays.format(toWedding) + " " + getString(R.string.days) + "\n" + simpleDateFormatHours.format(toWedding));
                        }
                    }
                });
            } else {
                chronometerToWedding.setVisibility(View.INVISIBLE);
                textViewToWedding.setVisibility(View.INVISIBLE);
                Toast.makeText(this, R.string.alarm_time_passed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takeTimeFromSP(){
        mYear = sharedPreferences.getInt(WeddingYear, 0);
        mMonth = sharedPreferences.getInt(WeddingMonth, 0);
        mDay = sharedPreferences.getInt(WeddingDay, 0);
        mHour = sharedPreferences.getInt(WeddingHour, 0);
        mMinute = sharedPreferences.getInt(WeddingMinutes, 0);

        int mMonthForShow = mMonth + 1;

        if (mYear != 0 && mHour != 0){
            textViewDate.setText(mDay + "-" + mMonthForShow + "-" + mYear);
            if (mMinute < 10){
                textViewTime.setText(mHour + ":" + "0" + mMinute);
            } else {
                textViewTime.setText(mHour + ":" + mMinute);
            }
            calendarSetTime = Calendar.getInstance();
            calendarSetTime.set(mYear, mMonth, mDay, mHour, mMinute, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Button buttonChangeDate = (Button) findViewById(R.id.buttonChangeDate);
        Button buttonChangeTime = (Button) findViewById(R.id.buttonChangeTime);
        Button buttonTurnOnAlarm = (Button) findViewById(R.id.buttonTurnoffAlarm);
        Button buttonTurnOffAlarm = (Button) findViewById(R.id.buttonTurnOnAlarm);
        chronometerToWedding = (Chronometer) findViewById(R.id.chronometerToWedding);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewToWedding = (TextView) findViewById(R.id.textViewToWedding);

        startChronometer();

        buttonChangeDate.setOnClickListener(this);
        buttonChangeTime.setOnClickListener(this);
        buttonTurnOnAlarm.setOnClickListener(this);
        buttonTurnOffAlarm.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chronometerToWedding.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chronometerToWedding.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_app_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent startAbout = new Intent(this, AboutActivity.class);
            startActivity(startAbout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int buttonId = v.getId();
        Calendar currentDate = Calendar.getInstance();

        switch (buttonId){
            case (R.id.buttonChangeDate):
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String weddingDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                textViewDate.setText(weddingDate);
                                editor.putInt(WeddingYear, year);
                                editor.putInt(WeddingMonth, monthOfYear);
                                editor.putInt(WeddingDay, dayOfMonth);
                                editor.commit();
                                startChronometer();
                            }
                        }, mYear, mMonth, mDay);
                dpd.updateDate(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case (R.id.buttonChangeTime):
                TimePickerDialog tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String weddingTime = hourOfDay + ":" + minute;
                                textViewTime.setText(weddingTime);
                                editor.putInt(WeddingHour, hourOfDay);
                                editor.putInt(WeddingMinutes, minute);
                                editor.commit();
                                if(sharedPreferences.getInt(WeddingYear, 0) != 0){
                                    startChronometer();
                                }
                            }
                        }, mHour, mMinute, true);
                tpd.show();
                break;
            case (R.id.buttonTurnOnAlarm):
                if (calendarSetTime != null) {

                    if (calendarSetTime.getTimeInMillis() > currentDate.getTimeInMillis()){
                        if (!AlarmSettings.isTimeAlarmSet(this)) {
                            setWeatherAlarm();
                            Toast.makeText(this, R.string.toast_alarm_turn_on, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, R.string.toast_alarm_turned_on, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.alarm_time_passed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.toast_time_or_hour_not_set, Toast.LENGTH_SHORT).show();
                }
                break;
            case (R.id.buttonTurnoffAlarm):
                if (calendarSetTime != null){
                    cancelAlarm();
                } else {
                    Toast.makeText(this, R.string.toast_time_or_hour_not_set, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, R.string.toast_alarm_turn_off, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}