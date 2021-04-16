package com.yusril.githubuser2.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.yusril.githubuser2.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REPEATING = "RepeatingAlarm"
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra(TYPE_REPEATING, 0)
        when (notifId) {
            ID_REPEATING -> {
                showAlarmNotification(context, notifId)
            }
        }

    }

    private fun showAlarmNotification(context: Context, notifId: Int) {
        val channelId = "channel09"
        val channelName = "Alarm Channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setContentTitle(context.resources.getString(R.string.daily_reminder))
            .setContentText(context.resources.getString(R.string.message_reminder))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(TYPE_REPEATING, ID_REPEATING)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 13)
        calendar.set(Calendar.MINUTE, 56)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent)

        Toast.makeText(context, R.string.setup_alarm, Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val idRepeating = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, idRepeating, intent, 0)

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, R.string.alarm_off, Toast.LENGTH_SHORT).show()
    }

}