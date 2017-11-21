package com.imgurisnotimgur.api

import okhttp3.ResponseBody

class ImgurApi {
    companion object {
        fun getImageFile(id: String): ByteArray {
            val response = HttpUtils.createRequest("https://i.imgur.com/$id.jpg", mapOf())
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }
    }
}