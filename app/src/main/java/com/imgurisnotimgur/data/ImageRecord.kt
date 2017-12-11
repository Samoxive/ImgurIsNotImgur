package com.imgurisnotimgur.data

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri

class ImageRecord(val id: String, val file: ByteArray) {
    companion object {
        val CONTENT_URI = ImgurContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(ImgurContract.PATH_IMAGE)
                .build()

        val TABLE_NAME = "image"
        val COLUMN_ID = "_id"
        val COLUMN_FILE = "file"

        val COLUMNS = arrayOf(COLUMN_ID, COLUMN_FILE)

        val CREATE_TABLE_SQL = "CREATE TABLE $TABLE_NAME (\n" +
                "$COLUMN_ID TEXT PRIMARY\n" +
                "$COLUMN_FILE BLOB NOT NULL\n" +
                ");"

        fun buildImageUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        fun getInstanceFromCursor(cursor: Cursor): ImageRecord = ImageRecord(cursor.getString(0),
                                                                             cursor.getBlob(1))
    }
}