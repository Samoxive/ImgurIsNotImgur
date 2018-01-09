package com.imgurisnotimgur.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootupListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) =
            CommentListener.initJob(context)
}
