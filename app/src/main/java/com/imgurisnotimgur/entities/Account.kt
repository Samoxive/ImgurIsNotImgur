package com.imgurisnotimgur.entities

data class Account(
        val id: Int,
        val url: String,
        val bio: String,
        val reputation: Double,
        val created: Int,
        val pro_expiration: Boolean
)