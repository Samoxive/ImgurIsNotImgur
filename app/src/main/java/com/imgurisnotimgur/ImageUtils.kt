package com.imgurisnotimgur

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ImageUtils {
    companion object {
        fun getScaledDownBitmap(file: ByteArray): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(file, 0, file.size, options)
            options.inJustDecodeBounds = false

            return if (options.outWidth > 1080) {
                val scale = options.outWidth.toFloat() / 1080.0
                options.inSampleSize = scale.toInt()

                BitmapFactory.decodeByteArray(file, 0, file.size, options)
            } else {
                BitmapFactory.decodeByteArray(file, 0, file.size, options)
            }
        }
    }
}