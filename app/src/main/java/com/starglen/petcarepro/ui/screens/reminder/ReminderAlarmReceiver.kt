package com.starglen.petcarepro.ui.screens.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.starglen.petcarepro.ui.screens.reminder.ReminderNotificationHelper

class ReminderAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Pet Reminder"
        val message = intent.getStringExtra("message") ?: "It's time to care for your pet"

        ReminderNotificationHelper.showNotification(
            context = context,
            title = title,
            message = message,
            id = System.currentTimeMillis().toInt() // Unique ID
        )
    }
}
