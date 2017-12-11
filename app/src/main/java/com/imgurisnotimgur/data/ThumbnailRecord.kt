package com.imgurisnotimgur.data

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri

class ThumbnailRecord(val id: String, val file: ByteArray) {
    companion object {
        val CONTENT_URI = ImgurContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(ImgurContract.PATH_THUMBNAIL)
                .build()
        val CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/${ImgurContract.CONTENT_AUTHORITY}/${ImgurContract.PATH_THUMBNAIL}"
        val CONTENT_ITEM_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/${ImgurContract.CONTENT_AUTHORITY}/${ImgurContract.PATH_THUMBNAIL}"

        val TABLE_NAME = "thumbnail"
        val COLUMN_ID = "_id"
        val COLUMN_FILE = "file"

        val COLUMNS = arrayOf(COLUMN_ID, COLUMN_FILE)

        val CREATE_TABLE_SQL = "CREATE TABLE $TABLE_NAME (\n" +
                "$COLUMN_ID TEXT PRIMARY\n" +
                "$COLUMN_FILE BLOB NOT NULL\n" +
                ");"


        fun buildThumbnailUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        fun getInstanceFromCursor(cursor: Cursor): ThumbnailRecord = ThumbnailRecord(cursor.getString(0),
                                                                                     cursor.getBlob(1))
    }
}