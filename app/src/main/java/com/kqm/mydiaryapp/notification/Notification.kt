package com.kqm.mydiaryapp.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.core.app.NotificationCompat
import com.kqm.mydiaryapp.R
import com.kqm.mydiaryapp.framework.parseIdRelation
import com.kqm.mydiaryapp.notification.Notification.Companion.NOTIFICATION_ID
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class Notification : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context = context, intent = intent)
    }

    private fun createNotification(context: Context, intent: Intent) {
        val noteText = intent.getStringExtra("noteText")
        val icon = Icon.createWithResource(context, R.drawable.calendar_icon_small)
        val notification = NotificationCompat.Builder(context, "Channel_Quote")
            .setSmallIcon(R.drawable.ic_calendar_small)
            .setLargeIcon(icon)
            .setContentTitle("¡RECORDATORIO!")
            .setContentText("Mañana tienes: $noteText")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ScheduleExactAlarm")
fun startNotification(context: Context, dayId: String, timePickerState: TimePickerState, noteText: String) {
    val intent = Intent(context, Notification::class.java).apply {
        putExtra("noteText", noteText)
    }

    val triggerTime = getTimeNotification(dayId, timePickerState)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setAlarmClock(
        AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
        pendingIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun getTimeNotification(dayId: String, timePickerState: TimePickerState): Long {
    val (day, month, year) = parseIdRelation(dayId)

    val monthValue = getNumberMonth(month)

    val triggerTime = LocalDateTime.of(year, monthValue, day, timePickerState.hour, timePickerState.minute)
        .minusDays(1)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    return triggerTime
}

private fun getNumberMonth(month: String): Int {
    return when (month) {
        "Enero" -> 1
        "Febrero" -> 2
        "Marzo" -> 3
        "Abril" -> 4
        "Mayo" -> 5
        "Junio" -> 6
        "Julio" -> 7
        "Agosto" -> 8
        "Septiembre" -> 9
        "Octubre" -> 10
        "Noviembre" -> 11
        "Diciembre" -> 12
        else -> throw IllegalArgumentException("Invalid month: $month")
    }
}

