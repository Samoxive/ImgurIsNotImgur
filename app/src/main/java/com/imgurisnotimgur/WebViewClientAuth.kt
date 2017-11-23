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
            val parameters = try {
                UrlUtils.parseOauthRedirectionUrl(url)
            } catch (e: OauthFailedException) {
                null
            }

            if (parameters == null) {
                // reload imgur page until user logs in successfully
                val authWebView = activity.findViewById<WebView>(R.id.authWebView)
                val authUrl = activity.OAUTH_LINK

                authWebView.loadUrl(authUrl)
                return
            }

            SecretUtils.saveSecrets(context, parameters)

            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}