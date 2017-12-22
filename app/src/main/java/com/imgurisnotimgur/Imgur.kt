package com.imgurisnotimgur

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.data.CommentRecord
import com.imgurisnotimgur.data.ImageRecord
import com.imgurisnotimgur.data.ThumbnailRecord
import com.imgurisnotimgur.entities.Comment
import com.imgurisnotimgur.entities.Image

class Imgur {
    companion object {
        fun getImageFile(contentResolver: ContentResolver, cursor: Cursor?, imageId: String): ByteArray {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val imageFile = cursor.getBlob(1)
                cursor.close()
                return imageFile
            }

            val imageFile = ImgurApi.getImageFile(imageId)

            if (imageFile.size < 1024 * 1024 * 5) {
                val dbValues = ContentValues()
                dbValues.put(ImageRecord.COLUMN_ID, imageId)
                dbValues.put(ImageRecord.COLUMN_FILE, imageFile)

                contentResolver.insert(ImageRecord.CONTENT_URI, dbValues)
            }

            return imageFile
        }

        fun getThumbnailFile(contentResolver: ContentResolver, thumbId: String): ByteArray {
            val cursor = contentResolver.query(ThumbnailRecord.buildThumbnailUri(thumbId), ThumbnailRecord.COLUMNS, null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val thumbFile = cursor.getBlob(1)
                cursor.close()
                return thumbFile
            }

            val thumbFile = ImgurApi.getThumbnailFile(thumbId)
            val dbValues = ContentValues()
            dbValues.put(ThumbnailRecord.COLUMN_ID, thumbId)
            dbValues.put(ThumbnailRecord.COLUMN_FILE, thumbFile)

            contentResolver.insert(ThumbnailRecord.CONTENT_URI, dbValues)
            return thumbFile
        }

        fun getComments(contentResolver: ContentResolver, cursor: Cursor?, image: Image): Array<Comment> {
            val id = if (image.isAlbum) { image.albumId } else { image.id }

            if (cursor != null && cursor.count > 0) {
                val comments = CommentRecord.getCommentsFromCursor(cursor)
                cursor.close()
                return comments
            }

            val comments = ImgurApi.getComments(image)
            val commentsDb = comments.map {
                val values = ContentValues()
                values.put(CommentRecord.COLUMN_ID, it.id)
                values.put(CommentRecord.COLUMN_IMAGEID, id)
                values.put(CommentRecord.COLUMN_CONTENT, it.content)
                values.put(CommentRecord.COLUMN_CREATED_AT, it.createdAt)
                values.put(CommentRecord.COLUMN_POINTS, it.points)
                values.put(CommentRecord.COLUMN_AUTHOR, it.author)
                return@map values
            }.toTypedArray()

            contentResolver.bulkInsert(CommentRecord.CONTENT_URI, commentsDb)
            return comments
        }
    }
}