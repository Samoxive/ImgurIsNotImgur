package com.imgurisnotimgur

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

class Notification {
    companion object {
    private val NOTIFICATION_ID = 1998
    private val NOTIFICATION_CHANNEL_ID = "reminder_notification_channel"
    private val ACTION_COPY_PENDING_INTENT_ID = 98
    private val PENDING_INTENT_ID = 38

        /*fun clearAllNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }*/

    @TargetApi(Build.VERSION_CODES.O)
    fun uploadedNotification(context: Context, url: String) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(mChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.image_uploaded_success))
                .setContentText(context.getString(R.string.image_uploaded))
                //.setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .addAction(copyToClipboard(context, url))
                //TODO: .addAction(openWithINIorBrowser(context,url))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun copyToClipboard(context: Context, url: String): NotificationCompat.Action {

        val copyToClipboardIntent = Intent(context, NotificationIntents::class.java)
        copyToClipboardIntent.action = "copy-to-clipboard"
        copyToClipboardIntent.putExtra("copyUrl",url)

        val ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_COPY_PENDING_INTENT_ID,
                copyToClipboardIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Action(R.drawable.ic_content_copy_black_24dp,
                "Copy to Clipboard",
                ignoreReminderPendingIntent)
    }

    private fun contentIntent(context: Context): PendingIntent {
        val startActivityIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun largeIcon(context: Context): Bitmap {
        val res = context.resources
        return BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_foreground)
    }
    }
}

