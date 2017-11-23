package com.imgurisnotimgur.api

import com.google.gson.Gson
import com.imgurisnotimgur.entities.Image
import com.imgurisnotimgur.entities.ImgurGalleryAlbum
import com.imgurisnotimgur.entities.SubredditImage
import okhttp3.ResponseBody
import org.json.JSONObject
import java.security.InvalidParameterException

class ImgurApi {
    companion object {
        val clientId = "7333a4b592aab44"
        val clientSecret = "07dd18a125014c99422a3de9e33cfb50ff5a1785"
        val thumbnailMode = "m"

        private fun getJsonData(jsonResponse: String): String {
            val responseObject = JSONObject(jsonResponse)
            if (responseObject.getBoolean("success")) {
                return responseObject.get("data").toString()
            } else {
                throw InvalidParameterException("Invalid Imgur Response")
            }
        }

        fun getImageFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun getThumbnailFile(id: String): ByteArray {
            val request = HttpUtils.createRequest("https://i.imgur.com/$id$thumbnailMode.jpg", mapOf())
            val response = HttpUtils.sendRequest(request)
            val body = response.body()
            return when (body) {
                is ResponseBody -> body.bytes()
                else -> byteArrayOf() // body is null
            }
        }

        fun getGallery(section: String, sort: String, nsfwEnabled: Boolean): Array<Image> {
            val sectionParam = section.toLowerCase()
            val sortParam = when (sort) {
                "Viral" -> "viral"
                "Time" -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                "Top (today)" -> "day"
                "Top (this week)" -> "week"
                "Top (this month)" -> "month"
                "Top (this year)" -> "year"
                "Top (all time)" -> "all"
                else -> ""
            }

            var url = "https://api.imgur.com/3/gallery/$sectionParam/$sortParam/"
            if (!timeWindow.equals("")) {
                url += "$timeWindow/"
            }

            url += "?mature=$nsfwEnabled&album_previews=true"

            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID $clientId"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val gallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
            val galleryImages = gallery.map {
                 Image (
                        if (it.is_album) { it.cover } else { it.id },
                        it.title,
                        it.account_url,
                        it.points,
                        it.datetime
                )
            }

            return galleryImages.toTypedArray()
        }

        fun getSubredditGallery(subreddit: String): Array<SubredditImage> {
            val url = "https://api.imgur.com/3/gallery/r/$subreddit/top/week/"
            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID $clientId"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val subredditGallery = gson.fromJson(imgurJson, Array<ImgurGalleryAlbum>::class.java)
            val subredditImages = subredditGallery.map {
                SubredditImage (
                        if (it.is_album) { it.cover } else { it.id },
                        it.title,
                        it.datetime
                )
            }

            return subredditImages.toTypedArray()
        }
    }
}