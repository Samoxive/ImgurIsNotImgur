package com.imgurisnotimgur

import okhttp3.HttpUrl
import java.util.*
import kotlin.collections.HashMap
import com.imgurisnotimgur.exceptions.OauthFailedException

class UrlUtils {
    companion object {
        fun parseQueryParameters(rawParameters: String): Map<String, String> {
            val keyValues = rawParameters.split("&")
            val store = HashMap<String, String>()

            for (pair in keyValues) {
                val splitKeyValue = pair.split("=")
                store.put(splitKeyValue[0], splitKeyValue[1])
            }

            return store
        }

        fun parseOauthRedirectionUrl(url: String): OauthResult {
            val fragmentString = HttpUrl.parse(url)!!.encodedFragment()
            fragmentString ?: throw OauthFailedException()

            val parameters = parseQueryParameters(fragmentString)
            val error = parameters["error"]

            if (error != null) {
                throw OauthFailedException()
            }

            val accessToken = parameters["access_token"]!!
            val refreshToken = parameters["refresh_token"]!!
            val accountId = parameters["account_id"]!!
            val accountUsername = parameters["account_username"]!!
            val expiresIn = (parameters["expires_in"])!!.toLong()
            val now = System.currentTimeMillis()
            val expirationDate = Date(now + expiresIn * 1000)

            return OauthResult (
                    accessToken,
                    refreshToken,
                    expirationDate,
                    accountId,
                    accountUsername
            )
        }




    }

}

