package com.imgurisnotimgur.entities

data class ImgurAccount (
        val id: Int,
        val url: String,
        val bio: String,
        val reputation: Double,
        val created: Long,
        val pro_expiration: Boolean
)