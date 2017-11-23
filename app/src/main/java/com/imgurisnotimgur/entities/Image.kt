package com.imgurisnotimgur.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Image (
        val id: String,
        val title: String,
        val username: String,
        val points: Int,
        val createdAt: Long,
        val albumId: String,
        val isAlbum: Boolean
): Parcelable