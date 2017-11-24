package com.imgurisnotimgur.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

class HttpUtils {
    companion object {

        val clientBuilder = OkHttpClient.Builder()

        init{
            clientBuilder.writeTimeout(30, TimeUnit.SECONDS)
            clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        }

        val client = clientBuilder.build()

        fun createRequest(method: HttpMethod, url: String, headers: Map<String, String>, body: RequestBody): Request {
            val builder = Request.Builder()
                    .url(url)

            when (method) {
                HttpMethod.DELETE -> builder.delete(body)
                HttpMethod.PUT -> builder.put(body)
                HttpMethod.POST -> builder.post(body)
                HttpMethod.GET -> builder.get()
            }

            for ((k, v) in headers) {
                builder.addHeader(k, v)
            }

            return builder.build()
        }

        fun createRequest(url: String, headers: Map<String, String>): Request {
            val builder = Request.Builder()
            builder.url(url)

            for ((k, v) in headers) {
                builder.addHeader(k, v)
            }

            return builder.build()
        }

        fun sendRequest(request: Request): Response = client.newCall(request).execute()
    }
}