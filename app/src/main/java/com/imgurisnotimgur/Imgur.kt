package com.imgurisnotimgur

import android.content.ContentResolver
import android.content.ContentValues
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.data.ImageRecord
import com.imgurisnotimgur.data.ImgurContract
import com.imgurisnotimgur.data.ThumbnailRecord

class Imgur {
    companion object {
        fun getImageFile(contentResolver: ContentResolver, imageId: String): ByteArray {
            val cursor = contentResolver.query(ImageRecord.buildImageUri(imageId), ImageRecord.COLUMNS, null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val imageFile = cursor.getBlob(1)
                cursor.close()
                return imageFile
            }

            val imageFile = ImgurApi.getImageFile(imageId)
            val dbValues = ContentValues()
            dbValues.put(ImageRecord.COLUMN_ID, imageId)
            dbValues.put(ImageRecord.COLUMN_FILE, imageFile)

            contentResolver.insert(ImageRecord.CONTENT_URI, dbValues)
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
    }
}