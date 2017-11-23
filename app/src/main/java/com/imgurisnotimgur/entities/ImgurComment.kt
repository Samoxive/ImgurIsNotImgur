package com.imgurisnotimgur.entities

data class ImgurComment (
        val id: Int,
        val image_id: String,
        val comment: String,
        val author: String,
        val author_id: Int,
        val on_album: Boolean,
        val album_cover: String,
        val ups: Int,
        val downs: Int,
        val points: Int,
        val datetime: Long,
        val parent_id: Int,
        val deleted: Boolean,
        val vote: String,
        val children: Array<ImgurComment>
)