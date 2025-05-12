package com.starglen.petcarepro.ui.screens.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import java.util.Calendar

object ReminderScheduler {
    fun scheduleReminder(
        context: Context,
        title: String,
        message: String,
        time: String,
        scheduledDate: String? = null
    ) {
        // If the Android version is 12 (API 31) or above, check for the exact alarm permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
                // Show message or prompt the user to enable the permission
                Toast.makeText(context, "Please grant permission to schedule exact alarms.", Toast.LENGTH_LONG).show()

                // Redirect the user to the app's settings page
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = android.net.Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
                return
            }
        }

        val calendar = Calendar.getInstance()

        // Extract time parts manually
        val parts = time.split(" ")
        val hm = parts[0].split(":")
        val hour = hm[0].toInt()
        val minute = hm[1].toInt()
        val isPM = parts[1].equals("PM", ignoreCase = true)

        val finalHour = if (isPM && hour < 12) hour + 12 else if (!isPM && hour == 12) 0 else hour

        calendar.set(Calendar.HOUR_OF_DAY, finalHour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Set date if provided
        scheduledDate?.let {
            val dateParts = it.split("/")
            if (dateParts.size == 3) {
                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt() - 1 // Calendar month is 0-based
                val year = dateParts[2].toInt()

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
            }
        }

        // If alarm time is in the past, push it forward to the next day
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val requestCode = (title + message + time).hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set the alarm for Android 10 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
