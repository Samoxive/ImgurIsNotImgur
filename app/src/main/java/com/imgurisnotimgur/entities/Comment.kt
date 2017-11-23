package com.imgurisnotimgur.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Comment (
    val id: Int,
    val content: String,
    val createdAt: Long,
    val points: Int,
    val author: String
) : Parcelable