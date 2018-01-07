package com.imgurisnotimgur.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import com.imgurisnotimgur.AsyncAction
import com.imgurisnotimgur.Imgur
import com.imgurisnotimgur.PreferenceUtils
import com.imgurisnotimgur.api.ImgurApi

class ChargingListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val (sectionIndex, sortIndex, nsfwEnabled) = PreferenceUtils.getGalleryParameters(preferences, context.resources)
        val subredditName = preferences.getString("subreddit", "food")
        AsyncAction({
            val subredditImages = ImgurApi.getSubredditGallery(subredditName)
            val galleryImages = ImgurApi.getGallery(sectionIndex, sortIndex, nsfwEnabled)
            Imgur.getThumbnailFiles(context.contentResolver, subredditImages)
            Imgur.getThumbnailFiles(context.contentResolver, galleryImages)
        }, {})
    }
}
