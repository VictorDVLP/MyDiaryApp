package com.kqm.mydiaryapp.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.core.app.NotificationCompat
import com.kqm.mydiaryapp.R
import com.kqm.mydiaryapp.notification.Notification.Companion.NOTIFICATION_ID
import java.util.Calendar

class Notification : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent?) {
        createNotification(context = context)
    }

    private fun createNotification(context: Context) {
        val icon = Icon.createWithResource(context, R.drawable.calendar_icon_small)
        val notification = NotificationCompat.Builder(context, "Channel_Quote")
            .setSmallIcon(R.drawable.ic_calendar_small)
            .setLargeIcon(icon)
            .setContentTitle("Mañana tienes algo importante")
            .setContentText("Texto de cita")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}

@SuppressLint("ScheduleExactAlarm")
fun startNotification(context: Context) {
    val intent = Intent(context, Notification::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        Calendar.getInstance().timeInMillis + 15000,
        pendingIntent
    )
}
