package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import com.imgurisnotimgur.entities.ImgurAccount
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(sam): Revert this back to a main activity, ideally we should launch the gallery activity if there isn't a problem with the tokens
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("secret", Context.MODE_PRIVATE)
        val accessToken = preferences.getString("accessToken", null)
        val refreshToken = preferences.getString("refreshToken", null)
        val expirationDate = preferences.getLong("expirationDate", 0)
        val accountId = preferences.getString("accountId", null)
        val accountUsername = preferences.getString("accountUsername", null)
        val httpClient = OkHttpClient()
        val gson = Gson()
        val request = Request.Builder()
                .url("https://api.imgur.com/3/account/me")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .get()
                .build()

        if (accessToken == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val intent = Intent(this, SubredditActivity::class.java)

        AsyncAction<Unit, Array<Image>>({
            ImgurApi.getGallery("Hot", "Viral", false)
        }, { images ->
            val image = images[0]
            Log.d("", image.id)
        }).exec()

        startActivity(intent)


    }
}
