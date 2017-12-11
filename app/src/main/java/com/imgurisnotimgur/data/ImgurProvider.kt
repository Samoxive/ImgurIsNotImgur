package com.imgurisnotimgur.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class ImgurProvider : ContentProvider() {
    companion object {
        val THUMBNAIL = 100
        val THUMBNAIL_WITH_ID = 101
        val IMAGE = 322
        val IMAGE_WITH_ID = 323

        fun createUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = ImgurContract.CONTENT_AUTHORITY

            matcher.addURI(authority, ImgurContract.PATH_THUMBNAIL, THUMBNAIL)
            matcher.addURI(authority, ImgurContract.PATH_IMAGE, IMAGE)
            matcher.addURI(authority, "${ImgurContract.PATH_THUMBNAIL}/#", THUMBNAIL_WITH_ID)
            matcher.addURI(authority, "${ImgurContract.PATH_IMAGE}/#", IMAGE_WITH_ID)

            return matcher
        }
    }

    val matcher = createUriMatcher()
    var db = ImgurDbHelper(context)

    override fun onCreate(): Boolean = true
    override fun getType(uri: Uri): String = ""

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val table = when (matcher.match(uri)) {
            THUMBNAIL_WITH_ID -> ThumbnailRecord.TABLE_NAME
            IMAGE_WITH_ID -> ImageRecord.TABLE_NAME
            THUMBNAIL, IMAGE -> return -1
            else -> throw UnsupportedOperationException("Unknown uri")
        }

        return 1
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO: Implement this to handle requests to insert a new row.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // TODO: Implement this to handle query requests from clients.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        // TODO: Implement this to handle requests to update one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }
}
