package com.kqm.mydiaryapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Channel_Quote"
        val descriptionText = "Este canal sirve para las notificaciones de citas"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Channel_Quote", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}