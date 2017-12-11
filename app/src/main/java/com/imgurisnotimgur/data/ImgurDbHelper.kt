package com.imgurisnotimgur.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ImgurDbHelper(val context: Context): SQLiteOpenHelper(context, name, null, version) {
    companion object {
        val version = 42
        val name = "imgur.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ThumbnailRecord.CREATE_TABLE_SQL)
        db.execSQL(ImageRecord.CREATE_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE ${ThumbnailRecord.TABLE_NAME};")
        db.execSQL("DROP TABLE ${ImageRecord.TABLE_NAME};")

        onCreate(db)
    }
}