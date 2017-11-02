package com.imgurisnotimgur

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class WebViewClientAuth : WebViewClient {
    var context: Context

    constructor(context: Context) {
        this.context = context
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if (url!!.contains("safetyjim")) {
            Toast.makeText(context, url, Toast.LENGTH_LONG).show()
        }
    }
}