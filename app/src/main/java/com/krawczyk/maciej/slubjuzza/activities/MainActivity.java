package com.krawczyk.maciej.slubjuzza.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.krawczyk.maciej.slubjuzza.AlarmSettings;
import com.krawczyk.maciej.slubjuzza.R;
import com.krawczyk.maciej.slubjuzza.UtilsAndConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final SimpleDateFormat simpleDateFormatDays = new SimpleDateFormat("DD");
    private final SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("HH:mm:ss");

    private Chronometer chronometerToWedding;
    private TextView textViewToWedding;
    private TextView textViewSetDate;
    private Calendar calendarSetTime;
    private Calendar calendarSetTimeInGoodFormat;

    private void setWeddingAlarm() {
        AlarmSettings.setWeddingAlarm(this, true);

        Intent intent = new Intent(UtilsAndConstants.BROADCAST_NOTIFICATION_FROM_ALARM);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (calendarSetTime != null) {
            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTimeInMillis(System.currentTimeMillis());

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    private void cancelAlarm() {
        AlarmSettings.setWeddingAlarm(this, false);
        Intent intent = new Intent(UtilsAndConstants.BROADCAST_NOTIFICATION_FROM_ALARM);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.cancel(alarmIntent);
    }

    public static String getYearsToWedding(Calendar setDate) {
        Calendar currentDate = Calendar.getInstance();
        long yearToWeddingInMillis = setDate.getTimeInMillis() - currentDate.getTimeInMillis();
        long yearToWedding = setDate.get(Calendar.YEAR) - currentDate.get(Calendar.YEAR);

        Calendar test = Calendar.getInstance();
        test.setTimeInMillis(yearToWedding);
        String yearsToWedding = "";

        if (yearToWedding == 0) {
            yearsToWedding = "";
        } else if (yearToWedding == 1 && yearToWeddingInMillis > UtilsAndConstants.MILLISECONDS_IN_YEAR) {
            yearsToWedding = yearToWedding + " rok ";
        } else if (yearToWedding >= 2 && yearToWedding <= 4) {
            yearsToWedding = yearToWedding + " lata ";
        } else if (yearToWedding >= 5) {
            yearsToWedding = yearToWedding + " lat ";
        }
        return yearsToWedding;
    }

    private void startChronometer() {
        UtilsAndConstants utilsAndConstants = new UtilsAndConstants(getApplicationContext());
        calendarSetTime = utilsAndConstants.getTimeFromSharedPreferences();

        Calendar currentDate = Calendar.getInstance();

        if (calendarSetTime != null) {
            if (calendarSetTime.getTimeInMillis() > currentDate.getTimeInMillis()) {
                chronometerToWedding.setVisibility(View.VISIBLE);
                textViewToWedding.setVisibility(View.VISIBLE);
                textViewSetDate.setVisibility(View.GONE);

                calendarSetTimeInGoodFormat = Calendar.getInstance();
                calendarSetTimeInGoodFormat.setTimeInMillis(calendarSetTime.getTimeInMillis()
                        - UtilsAndConstants.MILLISECONDS_IN_HOUR - UtilsAndConstants.MILLISECONDS_IN_DAY);

                chronometerToWedding.setBase(calendarSetTimeInGoodFormat.getTimeInMillis());
                chronometerToWedding.start();

                chronometerToWedding.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        Calendar currentDate = Calendar.getInstance();
                        long toWedding = calendarSetTimeInGoodFormat.getTimeInMillis() - currentDate.getTimeInMillis();

                        if (simpleDateFormatDays.format(toWedding).equals(UtilsAndConstants.ONE_DAY_PATTERN)) {
                            chronometer.setText(getYearsToWedding(calendarSetTimeInGoodFormat) + "\n" + simpleDateFormatHours.format(toWedding));
                        } else {
                            chronometer.setText(getYearsToWedding(calendarSetTimeInGoodFormat) + simpleDateFormatDays.format(toWedding) + " " + getString(R.string.days) + "\n" + simpleDateFormatHours.format(toWedding));
                        }
                    }
                });
            } else {
                chronometerToWedding.setVisibility(View.GONE);
                textViewToWedding.setVisibility(View.GONE);
                textViewSetDate.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonTurnOnAlarm = (Button) findViewById(R.id.buttonTurnoffAlarm);
        Button buttonTurnOffAlarm = (Button) findViewById(R.id.buttonTurnOnAlarm);
        Button buttonChangeTime = (Button) findViewById(R.id.buttonChangeTime);
        chronometerToWedding = (Chronometer) findViewById(R.id.chronometerToWedding);
        textViewToWedding = (TextView) findViewById(R.id.textViewToWedding);
        textViewSetDate = (TextView) findViewById(R.id.textViewSetDate);

        buttonTurnOnAlarm.setOnClickListener(this);
        buttonTurnOffAlarm.setOnClickListener(this);
        buttonChangeTime.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startChronometer();
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

        switch (buttonId) {
            case (R.id.buttonChangeTime):
                Intent intentSetDateActivity = new Intent(getApplicationContext(), SetDateActivity.class);
                startActivity(intentSetDateActivity);
                break;
            case (R.id.buttonTurnOnAlarm):
                if (calendarSetTime != null) {

                    if (calendarSetTime.getTimeInMillis() > currentDate.getTimeInMillis()) {
                        if (!AlarmSettings.isTimeAlarmSet(this)) {
                            setWeddingAlarm();
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
                if (calendarSetTime != null) {
                    cancelAlarm();
                } else {
                    Toast.makeText(this, R.string.toast_time_or_hour_not_set, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, R.string.toast_alarm_turn_off, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}