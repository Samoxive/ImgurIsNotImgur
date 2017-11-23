package com.imgurisnotimgur.api

import com.google.gson.Gson
import com.imgurisnotimgur.entities.*
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
            val galleryImages = gallery
                    .filter { if (it.is_album) { it.cover != null } else { true } }
                    .map {
                 Image (
                         if (it.is_album) { it.cover } else { it.id },
                         it.title,
                         it.account_url,
                         it.points,
                         it.datetime,
                         if (it.is_album) { it.id } else { "" },
                         it.is_album
                 )
            }

            // Do you know why that filter is there? Let me tell you. It's because imgur api is a shameless liar.
            // So, appearently sometimes gallery items don't have a cover image despite them being an album
            // Because of that, if we want null safety, we need to get rid of albums without a cover image
            // And that folks is why you don't blindly trust API documentation.

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

        fun getComments(image: Image): Array<Comment> {
            val id = if (image.isAlbum) { image.albumId } else { image.id }
            val url = "https://api.imgur.com/3/gallery/$id/comments/best"
            val request = HttpUtils.createRequest(url, mapOf("Authorization" to "Client-ID $clientId"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val imgurJson = getJsonData(jsonResponse)
            val gson = Gson()
            val imgurComments = gson.fromJson(imgurJson, Array<ImgurComment>::class.java)
            val comments = imgurComments.map {
                Comment(
                        it.id,
                        it.comment,
                        it.datetime,
                        it.points,
                        it.author
                )
            }

            return comments.toTypedArray()
        }
    }
}