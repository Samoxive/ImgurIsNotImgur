package com.imgurisnotimgur

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {
    public val OAUTH_LINK = "https://api.imgur.com/oauth2/authorize?client_id=7333a4b592aab44&response_type=token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        authWebView.settings.javaScriptEnabled = true
        authWebView.webViewClient = WebViewClientAuth(this@AuthenticationActivity, this)
        authWebView.loadUrl(OAUTH_LINK)
    }
}
