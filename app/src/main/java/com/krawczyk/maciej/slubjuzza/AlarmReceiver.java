package com.krawczyk.maciej.slubjuzza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, TimeService.class);
        context.startService(intent);
    }
}