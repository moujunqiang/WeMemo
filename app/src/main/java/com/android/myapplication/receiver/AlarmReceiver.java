package com.android.myapplication.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.myapplication.AlarmActivity;
import com.android.myapplication.bean.GlobalValues;


public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager m_notificationMgr = null;
    private static final int NOTIFICATION_FLAG = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        m_notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getAction().equals(GlobalValues.TIMER_ACTION_REPEATING)) {
            Log.e("alarm_receiver", "周期闹钟");
        } else if (intent.getAction().equals(GlobalValues.TIMER_ACTION)) {
            Log.e("alarm_receiver", "周期闹钟");

            // 进入闹钟操作界面
            Intent alaramIntent = new Intent(context, AlarmActivity.class);
            alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String id = intent.getStringExtra("id");
            alaramIntent.putExtra("title", title);
            alaramIntent.putExtra("content", content);

            alaramIntent.putExtra("id", id);
            context.startActivity(alaramIntent);
        }
    }
}
