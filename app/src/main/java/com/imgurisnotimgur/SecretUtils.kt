package com.imgurisnotimgur

import android.content.Context
import java.util.*

class SecretUtils {
    companion object {
        fun saveSecrets(context: Context, oauthResult: OauthResult) {
            val prefs = context.getSharedPreferences("secret", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            editor.putString("accessToken", oauthResult.accessToken)
            editor.putString("refreshToken", oauthResult.refreshToken)
            editor.putLong("expirationDate", oauthResult.expirationDate.time)
            editor.putString("accountId", oauthResult.accountId)
            editor.putString("accountUsername", oauthResult.accountUsername)

            editor.apply()
        }

        fun getSecrets(context: Context): Pair<Boolean, OauthResult> {
            val prefs = context.getSharedPreferences("secret", Context.MODE_PRIVATE)
            val accessToken = prefs.getString("accessToken", null)
            val refreshToken = prefs.getString("refreshToken", null)
            val expirationDate = prefs.getLong("expirationDate", 0)
            val accountId = prefs.getString("accountId", null)
            val accountUsername = prefs.getString("accountUsername", null)
            val result = OauthResult(
                    accessToken,
                    refreshToken,
                    Date(expirationDate),
                    accountId,
                    accountUsername
            )

            return (accessToken != null) to result
        }
    }
}