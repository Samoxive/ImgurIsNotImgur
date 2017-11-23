package com.imgurisnotimgur

import java.util.*

// https://api.imgur.com/oauth2/empire-did-nothing-wrong.comp205.com#
// access_token=[redacted]
// &expires_in=315360000
// &token_type=bearer
// &refresh_token=[redacted]
// &account_username=[redacted]
// &account_id=[redacted]
data class OauthResult (
        val accessToken: String,
        val refreshToken: String,
        val expirationDate: Date,
        val accountId: String,
        val accountUsername: String
)