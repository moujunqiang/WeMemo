package com.android.myapplication.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.android.myapplication.bean.NoteBean;


/**
 * 闹钟定时工具类
 *
 */

public class AlarmTimer {

    /**
     * 设置周期性闹钟
     *
     * @param context
     * @param firstTime
     * @param cycTime
     * @param action
     * @param AlarmManagerType 闹钟的类型，常用的有5个值：AlarmManager.ELAPSED_REALTIME、
     *                         AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、
     *                         AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP
     */
    public static void setRepeatingAlarmTimer(Context context, long firstTime,
                                              long cycTime, String action, int AlarmManagerType) {
        Intent myIntent = new Intent();
        myIntent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManagerType, firstTime, cycTime, sender);
        //param1：闹钟类型，param1：闹钟首次执行时间，param1：闹钟两次执行的间隔时间，param1：闹钟响应动作。
    }

    /**
     * 设置定时闹钟
     *
     * @param context
     * @param cycTime
     * @param action
     * @param AlarmManagerType 闹钟的类型，常用的有5个值：AlarmManager.ELAPSED_REALTIME、
     *                         AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、
     *                         AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP
     */
    public static void setAlarmTimer(Context context, long cycTime,
                                     String action, int AlarmManagerType, int alarmId, NoteBean noteBean) {
        Intent myIntent = new Intent();
        myIntent.putExtra("title",noteBean.getTitle()+"");
        myIntent.putExtra("content",noteBean.getContent()+"");
        myIntent.putExtra("id",noteBean.getId()+"");

        //传递定时日期
        myIntent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, myIntent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManagerType, cycTime, sender);
    }

    /**
     * 取消闹钟
     *
     * @param context
     * @param action
     */
    public static void cancelAlarmTimer(Context context, String action, int alarmId) {
        Intent myIntent = new Intent();
        myIntent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, myIntent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
}
