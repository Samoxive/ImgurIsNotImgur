package com.imgurisnotimgur

import android.app.IntentService
import android.content.Intent

/**
 * Created by ozankaraali on 7.01.2018.
 */

class NotificationIntents : IntentService("NotificationIntents") {
    override fun onHandleIntent(intent: Intent?) {
        val action = intent!!.action
        val url = intent!!.extras.getString("copyUrl")
        NotificationTasks.executeTask(this, action, url)
    }
}