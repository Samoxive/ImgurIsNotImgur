package com.imgurisnotimgur

import android.app.IntentService
import android.content.Intent
import com.imgurisnotimgur.data.BookmarkRecords
import com.imgurisnotimgur.entities.Image

/**
 * Created by ozankaraali on 7.01.2018.
 */

class NotificationIntents : IntentService("NotificationIntents") {
    override fun onHandleIntent(intent: Intent) {
        val action = intent.action
        when (action) {
            NotificationTasks.OPEN_WITH_INI,
            NotificationTasks.COPY_TO_CLIPBOARD,
            NotificationTasks.SHARE_URL -> NotificationTasks.executeTask(this, action, intent.extras.getString("copyUrl"))
            NotificationTasks.VIEW_COMMENTS -> {
                val image = intent.getParcelableExtra<Image>("image")
                val newIntent = Intent(this, ImageDetailActivity::class.java)
                newIntent.putExtra("image", image)
                startActivity(newIntent)
            }
            NotificationTasks.UNBOOKMARK -> {
                val image = intent.getParcelableExtra<Image>("image")
                contentResolver.delete(BookmarkRecords.CONTENT_URI, "${BookmarkRecords.COLUMN_IMAGEID} = ?", arrayOf(image.id))
            }
        }
        Notification.clearAllNotifications(this)
    }
}