package com.imgurisnotimgur.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri

class ImgurContract {
    companion object {
        val CONTENT_AUTHORITY = "com.imgurisnotimgur"
        val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
        val PATH_THUMBNAIL = "thumbnail"
        val PATH_IMAGE = "image"
    }
}