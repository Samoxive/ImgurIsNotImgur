package com.imgurisnotimgur

import android.app.Activity
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

        fun getTimeString(activity: Activity, createdAt: Long): String {
            val secondsS = activity.getString(R.string.seconds)
            val minutesS = activity.getString(R.string.minutes)
            val hoursS = activity.getString(R.string.hours)
            val daysS = activity.getString(R.string.days)
            val monthsS = activity.getString(R.string.months)
            val yearsS = activity.getString(R.string.years)

            val now = System.currentTimeMillis() / 1000
            val delta = now - createdAt

            return if (delta < 0) {
                "N / A"
            } else if (delta < 60) {
                "$delta$secondsS"
            } else if (delta < 60 * 60) {
                val mins = delta / 60
                "$mins$minutesS"
            } else if (delta < 60 * 60 * 24) {
                val hours = delta / (60 * 60)
                "$hours$hoursS"
            } else if (delta < 60 * 60 * 24 * 30) {
                val days = delta / (60 * 60 * 24)
                "$days$daysS"
            } else if (delta < 60 * 60 * 24 * 30 * 365) {
                val months = delta / (60 * 60 * 24 * 30)
                "$months$monthsS"
            } else {
                val years = delta / (60 * 60 * 24 * 30 * 365)
                "$years$yearsS"
            }
        }
    }
}