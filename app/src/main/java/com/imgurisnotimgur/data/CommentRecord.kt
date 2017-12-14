package com.imgurisnotimgur.data

import android.database.Cursor
import android.net.Uri
import com.imgurisnotimgur.entities.Comment

class CommentRecord {
    companion object {
        val CONTENT_URI = ImgurContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(ImgurContract.PATH_COMMENT)
                .build()

        val TABLE_NAME = "comment"
        val COLUMN_ID = "_id"
        val COLUMN_IMAGEID = "imageid"
        val COLUMN_CONTENT = "content"
        val COLUMN_CREATED_AT = "createdat"
        val COLUMN_POINTS = "points"
        val COLUMN_AUTHOR = "author"

        val COLUMNS = arrayOf(
                COLUMN_ID,
                COLUMN_IMAGEID,
                COLUMN_CONTENT,
                COLUMN_CREATED_AT,
                COLUMN_POINTS,
                COLUMN_AUTHOR
        )

        val CREATE_TABLE_SQL = "CREATE TABLE comment (\n" +
                "$COLUMN_ID INTEGER PRIMARY KEY,\n" +
                "$COLUMN_IMAGEID TEXT,\n" +
                "$COLUMN_CONTENT TEXT,\n" +
                "$COLUMN_CREATED_AT INTEGER,\n" +
                "$COLUMN_POINTS INTEGER,\n" +
                "$COLUMN_AUTHOR TEXT,\n" +
                "FOREIGN KEY($COLUMN_IMAGEID) REFERENCES ${ImageRecord.TABLE_NAME}(${ImageRecord.COLUMN_ID})\n"+
                ")"

        fun buildImageCommentsUri(imageId: String): Uri = CONTENT_URI.buildUpon().appendPath(imageId).build()
        fun getCommentsFromCursor(cursor: Cursor): Array<Comment> {
            val array = Array(cursor.count, { Comment(0, "", 0, 0, "") })

            var i = 0
            while (cursor.moveToNext()) {
                array[i] = Comment(
                        cursor.getInt(0),
                        cursor.getString(2),
                        cursor.getInt(3).toLong(),
                        cursor.getInt(4),
                        cursor.getString(5)
                )

                i++
            }

            return array
        }
    }
}