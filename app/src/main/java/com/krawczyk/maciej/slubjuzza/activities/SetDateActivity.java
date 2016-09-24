package com.krawczyk.maciej.slubjuzza.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.krawczyk.maciej.slubjuzza.R;
import com.krawczyk.maciej.slubjuzza.UtilsAndConstants;

import java.util.Calendar;

public class SetDateActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String ZERO_FOR_MINUTES_STRING = "0";

    private TextView textViewDate;
    private TextView textViewTime;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        updateTextViews();

        Toast.makeText(getApplicationContext(), R.string.tap_to_edit_message, Toast.LENGTH_LONG).show();

        textViewDate.setOnClickListener(this);
        textViewTime.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        MenuItem About = menu.findItem(R.id.action_about);
        if (actionBar != null) {
            actionBar.setLogo(R.mipmap.ic_app_icon);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        About.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        UtilsAndConstants utilsAndConstants = new UtilsAndConstants(getApplicationContext());
        final Calendar savedDate = utilsAndConstants.getTimeFromSharedPreferences();

        final SharedPreferences sharedPreferences = getSharedPreferences(UtilsAndConstants.MY_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        switch (id) {
            case R.id.textViewDate:
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String weddingDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                textViewDate.setText(weddingDate);
                                sharedPreferencesEditor.putInt(UtilsAndConstants.WEDDING_YEAR, year);
                                sharedPreferencesEditor.putInt(UtilsAndConstants.WEDDING_MONTH, monthOfYear);
                                sharedPreferencesEditor.putInt(UtilsAndConstants.WEDDING_DAY, dayOfMonth);
                                sharedPreferencesEditor.apply();
                            }
                        }, savedDate.get(Calendar.YEAR), savedDate.get(Calendar.MONTH), savedDate.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.textViewTime:
                TimePickerDialog tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String weddingTime = improveMinutesOrHoursForShow(hourOfDay) + ":"
                                        + improveMinutesOrHoursForShow(minute);
                                textViewTime.setText(weddingTime);
                                sharedPreferencesEditor.putInt(UtilsAndConstants.WEDDING_HOUR, hourOfDay);
                                sharedPreferencesEditor.putInt(UtilsAndConstants.WEDDING_MINUTES, minute);
                                sharedPreferencesEditor.apply();
                            }
                        }, savedDate.get(Calendar.HOUR_OF_DAY), savedDate.get(Calendar.MINUTE), true);
                tpd.show();
                break;
            default:
                break;
        }
    }

    private void updateTextViews() {
        UtilsAndConstants utilsAndConstants = new UtilsAndConstants(getApplicationContext());
        Calendar calendarFromSharedPreferences = utilsAndConstants.getTimeFromSharedPreferences();

        textViewDate.setText(getDateString(calendarFromSharedPreferences));
        textViewTime.setText(getTimeString(calendarFromSharedPreferences));
    }

    private String getDateString(Calendar calendar) {
        int improvedMonth = calendar.get(Calendar.MONTH) + 1;
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                improvedMonth + "-" + calendar.get(Calendar.YEAR);
    }

    private String getTimeString(Calendar calendar) {
        return improveMinutesOrHoursForShow(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + improveMinutesOrHoursForShow(calendar.get(Calendar.MINUTE));
    }

    private String improveMinutesOrHoursForShow(int minutesOrHour) {
        if (minutesOrHour > 10) {
            return String.valueOf(minutesOrHour);
        }
        return ZERO_FOR_MINUTES_STRING + minutesOrHour;
    }
}