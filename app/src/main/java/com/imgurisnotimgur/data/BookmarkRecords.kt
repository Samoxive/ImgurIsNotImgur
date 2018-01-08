package com.imgurisnotimgur.data

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.imgurisnotimgur.entities.Image

class BookmarkRecords {
    companion object {
        val CONTENT_URI = ImgurContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(ImgurContract.PATH_BOOKMARK)
                .build()

        val TABLE_NAME = "bookmark"
        val COLUMN_ID = "_id"
        val COLUMN_IMAGEID = "imageid"
        val COLUMN_TITLE = "title"
        val COLUMN_USERNAME = "username"
        val COLUMN_POINTS = "points"
        val COLUMN_CREATEDAT = "createdat"
        val COLUMN_ALBUMID = "albumid"
        val COLUMN_ISALBUM = "isalbum"

        val COLUMNS = arrayOf(
                COLUMN_ID,
                COLUMN_IMAGEID,
                COLUMN_TITLE,
                COLUMN_USERNAME,
                COLUMN_POINTS,
                COLUMN_CREATEDAT,
                COLUMN_ALBUMID,
                COLUMN_ISALBUM
        )

        val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_IMAGEID TEXT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_POINTS INTEGER, " +
                "$COLUMN_CREATEDAT INTEGER, " +
                "$COLUMN_ALBUMID TEXT, " +
                "$COLUMN_ISALBUM INTEGER" +
                ")"

        fun buildBookmarkUri(imageId: String): Uri = CONTENT_URI.buildUpon().appendPath(imageId).build()

        fun getInstancesFromCursor(cursor: Cursor): List<Image> {
            val images = arrayListOf<Image>()
            while (cursor.moveToNext()) {
                images.add(Image(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getLong(4),
                        cursor.getLong(5),
                        cursor.getString(6),
                        cursor.getInt(7) == 1
                ))
            }
            return images
        }

        fun getContentValueFrom(image: Image): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_IMAGEID, image.id)
            values.put(COLUMN_TITLE, image.title)
            values.put(COLUMN_USERNAME, image.username)
            values.put(COLUMN_POINTS, image.points)
            values.put(COLUMN_CREATEDAT, image.createdAt)
            values.put(COLUMN_ALBUMID, image.albumId)
            values.put(COLUMN_ISALBUM, if (image.isAlbum) 1 else 0)
            return values
        }
    }
}