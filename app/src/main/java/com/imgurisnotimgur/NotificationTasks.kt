package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.util.Log
import com.imgurisnotimgur.api.ImgurApi


class NotificationTasks {
    companion object {
        val COPY_TO_CLIPBOARD = "copy-to-clipboard"
        val OPEN_WITH_INI = "open-with-ini"
        val SHARE_URL = "share-url"

        fun executeTask(context: Context, action: String, url:String) {
            if (COPY_TO_CLIPBOARD == action) {
                clipboardURL(context, url)
            } else if (OPEN_WITH_INI == action) {
                openWithINI(context, url)
            } else if (SHARE_URL == action) {
                shareURL(context, url)
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

        private fun openWithINI(context: Context, url: String) {
            val intent = Intent(context, ImageDetailActivity::class.java)
            val picId = (url.split('/')[3]).split('.')[0]
            intent.putExtra("image", ImgurApi.getMetadataFromId(picId, context))
            context.startActivity(intent)
        }

        private fun shareURL(context: Context, url: String) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, url)
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }
    }
}
