package com.imgurisnotimgur

import android.content.Context
import android.util.Log

class NotificationTasks {
    companion object {
        val COPY_TO_CLIPBOARD = "copy-to-clipboard"

        fun executeTask(context: Context, action: String, url:String) {
            if (COPY_TO_CLIPBOARD == action) {
                clipboardURL(context, url)
            }
        }
        private fun clipboardURL(context: Context, url: String) {
            try {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val cpyURL = android.content.ClipData.newPlainText("Copied URL", url)
                clipboard.primaryClip = cpyURL
            } catch (e: Exception) {
                Log.e("Clipboard", "Couldn't copy")
            }
        }
    }
}
