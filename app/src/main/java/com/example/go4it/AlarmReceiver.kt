package com.example.go4it

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

//Reference Link : https://www.tutorialspoint.com/how-to-implement-alarmmanager-in-android-using-kotlin

class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null
    private var notification = ""
    private var id: Short = 0
    private var proverb: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notification = "Looking for FingerLicking healthy food? Just Go4It!!"
        //Deliver the notification.
        deliverNotification(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deliverNotification(context: Context) {
        // Create the content intent for the notification, which launches
        // this activity
        val contentIntent = Intent(context, MainActivity::class.java)

        var contentPendingIntent:PendingIntent?=null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentPendingIntent = PendingIntent.getActivity(
                        context,
                NOTIFICATION_ID,
                contentIntent,
                PendingIntent.FLAG_MUTABLE
            )

        } else {
            contentPendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        }



        // Build the notification
        val builder: Notification.Builder? =
            Notification.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentPendingIntent)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(notification)
                )
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        // Deliver the notification
        notificationManager!!.notify(NOTIFICATION_ID, builder!!.build())
    }

    companion object {
        // Notification ID.
        private const val NOTIFICATION_ID = 0

        // Notification channel ID.
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}
