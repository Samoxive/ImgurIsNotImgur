package com.imgurisnotimgur.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class ImgurProvider : ContentProvider() {
    companion object {
        val THUMBNAIL = 100
        val IMAGE = 322

        fun createUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = ImgurContract.CONTENT_AUTHORITY

            matcher.addURI(authority, ImgurContract.PATH_THUMBNAIL, THUMBNAIL)
            matcher.addURI(authority, ImgurContract.PATH_IMAGE, IMAGE)

            return matcher
        }
    }

    val matcher = createUriMatcher()
    var db: ImgurDbHelper? = null

    override fun onCreate(): Boolean {
        db = ImgurDbHelper(context)
        return true
    }
    override fun getType(uri: Uri): String = ""

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val table = when (matcher.match(uri)) {
            THUMBNAIL -> ThumbnailRecord.TABLE_NAME
            IMAGE -> ImageRecord.TABLE_NAME
            else -> throw UnsupportedOperationException("Invalid uri")
        }

        val database = db!!.writableDatabase
        return database.delete(table, selection, selectionArgs)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = when (matcher.match(uri)) {
            THUMBNAIL -> ThumbnailRecord.TABLE_NAME
            IMAGE -> ImageRecord.TABLE_NAME
            else -> throw UnsupportedOperationException("Invalid uri")
        }

        val database = db!!.writableDatabase
        val id = database.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE)

        if (id <= 0) {
            throw SQLException("Could not insert row into $table")
        }

        return ImgurContract.BASE_CONTENT_URI.buildUpon().appendPath(table).appendPath(values!!.getAsString("_id")).build()
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val table = when (matcher.match(uri)) {
            THUMBNAIL -> ThumbnailRecord.TABLE_NAME
            IMAGE -> ImageRecord.TABLE_NAME
            else -> throw UnsupportedOperationException("Invalid uri")
        }

        val database = db!!.readableDatabase
        return database.query(table, projection, selection, selectionArgs, null, null, null)
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val table = when (matcher.match(uri)) {
            THUMBNAIL -> ThumbnailRecord.TABLE_NAME
            IMAGE -> ImageRecord.TABLE_NAME
            else -> throw UnsupportedOperationException("Invalid uri")
        }

        val database = db!!.writableDatabase
        return database.updateWithOnConflict(table, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_REPLACE)
    }
}
