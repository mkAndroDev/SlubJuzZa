package com.krawczyk.maciej.slubjuzza;

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
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String MyPreferences = "MyPrefs" ;
    private static final String DATE = "MyPrefs" ;
    private static final String TIME = "MyPrefs" ;
    private final SimpleDateFormat simpleDateFormatDays = new SimpleDateFormat("DD");
    private final SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("HH:mm:ss");

    private Chronometer chronometerToWedding;
    private TextView textViewDate;
    private TextView textViewTime;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Calendar calendar;

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

        buttonChangeDate.setOnClickListener(this);
        buttonChangeTime.setOnClickListener(this);
        buttonTurnOnAlarm.setOnClickListener(this);
        buttonTurnOffAlarm.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        textViewDate.setText(sharedPreferences.getString(DATE, ""));
        textViewTime.setText(sharedPreferences.getString(TIME, ""));

        calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.JUNE, 04, 15, 0, 0); //todo tu zmieniac

        chronometerToWedding.setBase(calendar.getTimeInMillis());
        chronometerToWedding.start();

        chronometerToWedding.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Calendar now = Calendar.getInstance();
                long toWedding = calendar.getTimeInMillis() - now.getTimeInMillis();
                chronometer.setText(simpleDateFormatDays.format(toWedding) + " " + getString(R.string.days) + " " + simpleDateFormatHours.format(toWedding));
            }
        });
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
        switch (buttonId){
            case (R.id.buttonChangeDate):
                break;
            case (R.id.buttonChangeTime):
                break;
            case (R.id.buttonTurnOnAlarm):
                Toast.makeText(this, R.string.toast_alarm_turn_on, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.buttonTurnoffAlarm):
                Toast.makeText(this, R.string.toast_alarm_turn_off, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
