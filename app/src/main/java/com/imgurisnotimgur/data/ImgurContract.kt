package com.imgurisnotimgur.data

import android.net.Uri

class ImgurContract {
    companion object {
        val CONTENT_AUTHORITY = "com.imgurisnotimgur"
        val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
        val PATH_THUMBNAIL = "thumbnail"
        val PATH_IMAGE = "image"
        val PATH_COMMENT = "comment"
        val PATH_BOOKMARK = "bookmark"
    }
}