package com.krawczyk.maciej.slubjuzza;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.krawczyk.maciej.slubjuzza.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeService extends IntentService {

    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD");

    private Calendar calendarSavedTime;

    public TimeService() {
        super("TimeReceiver");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        calendarSavedTime = Calendar.getInstance();
        UtilsAndConstants utilsAndConstants = new UtilsAndConstants(getApplicationContext());
        calendarSavedTime = utilsAndConstants.getTimeFromSharedPreferences();

        Calendar now = Calendar.getInstance();
        long toWedding = calendarSavedTime.getTimeInMillis() - now.getTimeInMillis();

        if (toWedding < UtilsAndConstants.MILLISECONDS_IN_DAY) {
            AlarmSettings.setWeddingAlarm(this, false);
            Intent intentForAlarm = new Intent(UtilsAndConstants.BROADCAST_NOTIFICATION_FROM_ALARM);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intentForAlarm, 0);

            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            alarmMgr.cancel(alarmIntent);
            return;
        } else {
            toWedding -= UtilsAndConstants.MILLISECONDS_IN_DAY;
        }

        String stayDays = simpleDateFormat.format(toWedding);

        String toShow;
        String toShowYear = utilsAndConstants.getYearsToWedding(calendarSavedTime);

        toShow = toShowYear + stayDays + " " + getString(R.string.days) + "!";

        intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification timeNotification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(toShow).setSmallIcon(R.mipmap.ic_app_icon)
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