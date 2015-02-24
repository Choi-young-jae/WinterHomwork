package com.example.user.cowaplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 2015-02-20.
 */
public class AlarmReceive extends BroadcastReceiver {
    public static int getalarmnum = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();

        int getalarmnum = bundle.getInt("alarmnum");

        Log.d(null,"Alram return " + getalarmnum + "!");


        Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();
        NotificationManager notifier = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification(android.R.drawable.btn_star, "text",
                System.currentTimeMillis());



        Bundle bundle2 = intent.getExtras();

        Intent intent2 = new Intent(context, CowDetailView.class);

        intent2.putExtras(bundle2);

        PendingIntent pender = PendingIntent
                .getActivity(context, getalarmnum, intent2, 0);
        Log.d("alarmcount ",getalarmnum + "");
        getalarmnum++;

        notify.setLatestEventInfo(context, bundle2.getString("data0"), bundle2.getString("data1"), pender);

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.vibrate = new long[] { 200, 200, 500, 300 };
        // notify.sound=Uri.parse("file:/");
        notify.number++;
        notifier.notify(getalarmnum, notify);
    }

}

