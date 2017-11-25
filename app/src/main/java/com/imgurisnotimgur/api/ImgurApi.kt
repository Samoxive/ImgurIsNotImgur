package com.imgurisnotimgur.api

import android.util.Base64
import com.google.gson.Gson
import com.imgurisnotimgur.SecretUtils
import com.imgurisnotimgur.entities.*
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
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

        fun getSelfAccount(accessToken: String): Account {
            val request = HttpUtils.createRequest("https://api.imgur.com/3/account/me", mapOf("Authorization" to "Bearer $accessToken"))
            val response = HttpUtils.sendRequest(request)
            val body = response.body()!!
            val jsonResponse = body.string()
            val gson = Gson()
            val imgurJson = getJsonData(jsonResponse)
            val imgurAccount = gson.fromJson(imgurJson, ImgurAccount::class.java)
            return Account(
                    imgurAccount.url,
                    if (imgurAccount.bio == null) { "" } else { imgurAccount.bio },
                    imgurAccount.created,
                    imgurAccount.reputation
            )
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

        fun getGallery(section: Int, sort: Int, nsfwEnabled: Boolean): Array<Image> {
            val sectionParam = when(section) {
                0 -> "hot"
                1 -> "top"
                2 -> "user"
                else -> "hot"
            }

            val sortParam = when (sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                2 -> "day"
                3 -> "week"
                4 -> "month"
                5 -> "year"
                6 -> "all"
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

        fun uploadImage(file: File, accessToken: String) {
            val bytes = file.readBytes()
            val base64Encoded = Base64.encodeToString(bytes, Base64.DEFAULT)

            val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", base64Encoded)
                    .build()

            val request = Request.Builder()
                    .url("https://api.imgur.com/3/image")
                    .header("Authorization", "Client-ID $clientId")
                    .header("Authorization", "Bearer $accessToken")
                    .post(body)
                    .build()

            val response = HttpUtils.sendRequest(request)
            // do stuff with this
        }

        fun getSearch(search: String, sort: Int): Array<Image> {

            val sortParam = when (sort) {
                0 -> "viral"
                1 -> "time"
                else -> "top" // Top (x)
            }

            val timeWindow = when (sort) {
                2 -> "day"
                3 -> "week"
                4 -> "month"
                5 -> "year"
                6 -> "all"
                else -> ""
            }

            var url = "https://api.imgur.com/3/gallery/search/$sortParam/"
            if (!timeWindow.equals("")) {
                url += "$timeWindow"
            }

            url += "?q=$search"

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

            return galleryImages.toTypedArray()
        }
    }
}