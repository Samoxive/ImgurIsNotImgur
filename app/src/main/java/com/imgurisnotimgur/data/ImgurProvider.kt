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
        val THUMBNAIL_WITH_ID = 101
        val IMAGE = 322
        val IMAGE_WITH_ID = 323
        val COMMENT = 42
        val COMMENT_WITH_IMAGE_ID = 7
        val EVERYTHING = 1337

        fun createUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = ImgurContract.CONTENT_AUTHORITY

            matcher.addURI(authority, ImgurContract.PATH_THUMBNAIL, THUMBNAIL)
            matcher.addURI(authority, ImgurContract.PATH_IMAGE, IMAGE)
            matcher.addURI(authority, "${ImgurContract.PATH_THUMBNAIL}/*", THUMBNAIL_WITH_ID)
            matcher.addURI(authority, "${ImgurContract.PATH_IMAGE}/*", IMAGE_WITH_ID)
            matcher.addURI(authority, ImgurContract.PATH_COMMENT, COMMENT)
            matcher.addURI(authority, "${ImgurContract.PATH_COMMENT}/*", COMMENT_WITH_IMAGE_ID)
            matcher.addURI(authority, "everything", EVERYTHING)

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

    fun deletThis() {
        val database = db!!.writableDatabase
        db?.onUpgrade(database, 0, 0)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val (table, sel, selArgs) = when (matcher.match(uri)) {
            THUMBNAIL_WITH_ID -> Triple(ThumbnailRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            IMAGE_WITH_ID -> Triple(ImageRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            COMMENT_WITH_IMAGE_ID -> Triple(CommentRecord.TABLE_NAME, "imageid = ?", arrayOf(uri.lastPathSegment))
            THUMBNAIL -> Triple(ThumbnailRecord.TABLE_NAME, selection, selectionArgs)
            IMAGE -> Triple(ImageRecord.TABLE_NAME, selection, selectionArgs)
            COMMENT -> Triple(CommentRecord.TABLE_NAME, selection, selectionArgs)
            EVERYTHING -> {
                deletThis()
                return 9001
            }
            else -> throw UnsupportedOperationException("Unknown uri")
        }

        val database = db!!.writableDatabase
        return database.delete(table, sel, selArgs)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = when (matcher.match(uri)) {
            THUMBNAIL -> ThumbnailRecord.TABLE_NAME
            IMAGE -> ImageRecord.TABLE_NAME
            COMMENT -> CommentRecord.TABLE_NAME
            else -> throw UnsupportedOperationException("Invalid uri")
        }

        val database = db!!.writableDatabase
        val id = database.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE)

        if (id <= 0) {
            throw SQLException("Could not insert row into $table")
        }

        return ImgurContract.BASE_CONTENT_URI.buildUpon().appendPath(table).appendPath(values!!.getAsString("_id")).build()
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val (table, sel, selArgs) = when (matcher.match(uri)) {
            THUMBNAIL_WITH_ID -> Triple(ThumbnailRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            IMAGE_WITH_ID -> Triple(ImageRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            COMMENT_WITH_IMAGE_ID -> Triple(CommentRecord.TABLE_NAME, "imageid = ?", arrayOf(uri.lastPathSegment))
            THUMBNAIL -> Triple(ThumbnailRecord.TABLE_NAME, selection, selectionArgs)
            IMAGE -> Triple(ImageRecord.TABLE_NAME, selection, selectionArgs)
            COMMENT -> Triple(CommentRecord.TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri")
        }
        
        val database = db!!.readableDatabase
        return database.query(table, projection, sel, selArgs, null, null, sortOrder)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val (table, sel, selArgs) = when (matcher.match(uri)) {
            THUMBNAIL_WITH_ID -> Triple(ThumbnailRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            IMAGE_WITH_ID -> Triple(ImageRecord.TABLE_NAME, "_id = ?", arrayOf(uri.lastPathSegment))
            COMMENT_WITH_IMAGE_ID -> Triple(CommentRecord.TABLE_NAME, "imageid = ?", arrayOf(uri.lastPathSegment))
            THUMBNAIL -> Triple(ThumbnailRecord.TABLE_NAME, selection, selectionArgs)
            IMAGE -> Triple(ImageRecord.TABLE_NAME, selection, selectionArgs)
            COMMENT -> Triple(CommentRecord.TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri")
        }

        val database = db!!.writableDatabase
        return database.updateWithOnConflict(table, values, sel, selArgs, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        val match = matcher.match(uri)
        if (match != COMMENT) {
            return -1
        }

        val database = db!!.writableDatabase
        database.beginTransaction()

        for (value in values) {
            database.insert(CommentRecord.TABLE_NAME, null, value)
        }

        database.setTransactionSuccessful()
        database.endTransaction()

        return values.size
    }
}
