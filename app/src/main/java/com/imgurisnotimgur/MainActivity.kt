package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.imgurisnotimgur.entities.Account
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

        AsyncAction<Int, String>({ _ ->
            httpClient.newCall(request)
                    .execute()
                    .body()!!
                    .string()
        }, { jsonResponse ->
            val jsonObject = JSONObject(jsonResponse)
            val dataString = jsonObject.getJSONObject("data").toString()
            val account: Account = gson.fromJson(dataString, Account::class.java)
            // do account stuff
        }).execute(1)

        startActivity(intent)


    }
}
