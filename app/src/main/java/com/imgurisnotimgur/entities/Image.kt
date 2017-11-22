package com.imgurisnotimgur.entities

data class Image (
    val id: String,
    val title: String,
    val username: String,
    val points: Int,
    val createdAt: Long
)