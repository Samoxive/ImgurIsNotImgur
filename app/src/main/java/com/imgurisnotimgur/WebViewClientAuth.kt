package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import com.imgurisnotimgur.exceptions.OauthFailedException

class WebViewClientAuth(private val context: Context,
                        private val activity: AuthenticationActivity) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if (url!!.contains("empire")) {
            val prefEdit = context.getSharedPreferences("secret", Context.MODE_PRIVATE).edit()
            val parameters = try {
                UrlUtils.parseOauthRedirectionUrl(url)
            } catch (e: OauthFailedException) {
                null
            }

            if (parameters == null) {
                val authWebView = activity.findViewById<WebView>(R.id.authWebView)
                val authUrl = activity.OAUTH_LINK

                authWebView.loadUrl(authUrl)
                return
            }

            prefEdit.putString("accessToken", parameters.accessToken)
            prefEdit.putString("refreshToken", parameters.refreshToken)
            prefEdit.putLong("expirationDate", parameters.expirationDate.time)
            prefEdit.putString("accountId", parameters.accountId)
            prefEdit.putString("accountUsername", parameters.accountUsername)
            prefEdit.commit()

            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}