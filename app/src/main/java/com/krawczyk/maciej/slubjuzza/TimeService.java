package com.krawczyk.maciej.slubjuzza;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.krawczyk.maciej.slubjuzza.activities.MainActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeService extends IntentService {

    private static final String MyPreferences = "MyPrefs" ;
    private static final String WeddingYear = "WeddingYear" ;
    private static final String WeddingMonth = "WeddingMonth" ;
    private static final String WeddingDay = "WeddingDay" ;
    private static final String WeddingHour = "WeddingHour" ;
    private static final String WeddingMinutes = "WeddingMinutes" ;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private SharedPreferences sharedPreferences;
    private Calendar calendar;

    private Notification timeNotification = null;

    public TimeService(){
        super("TimeReceiver");
    }

    private void takeTimeFromSP(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreferences, MODE_PRIVATE);
        mYear = sharedPreferences.getInt(WeddingYear, 0);
        mMonth = sharedPreferences.getInt(WeddingMonth, 0);
        mDay = sharedPreferences.getInt(WeddingDay, 0);
        mHour = sharedPreferences.getInt(WeddingHour, 0);
        mMinute = sharedPreferences.getInt(WeddingMinutes, 0);

        if (mYear != 0 && mHour != 0){
            calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        calendar = Calendar.getInstance();
        takeTimeFromSP();
        calendar.set(mYear, mMonth, mDay, mHour-1, mMinute, 00);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD");
        Calendar now = Calendar.getInstance();
        long toTrip = calendar.getTimeInMillis() - now.getTimeInMillis();
        String stay = simpleDateFormat.format(toTrip);

        intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        timeNotification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(stay + "dni!").setSmallIcon(R.mipmap.ic_app_icon) //todo dorobić lata itp
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timeNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, timeNotification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}