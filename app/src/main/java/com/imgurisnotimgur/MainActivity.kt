package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("secret", Context.MODE_PRIVATE)
        val accessToken = preferences.getString("accessToken", null)
        val client = OkHttpClient()

        if (accessToken == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)
        } else {

            AsyncAction<Unit, Unit>({ Thread.sleep(3000) }, { test.text = "OI MATE LUL\n" + accessToken }).execute(Unit)
            AsyncAction<String, String>(
                    { url ->
                        Thread.sleep(3000)
                        val request = Request.Builder()
                                .addHeader("Authorization", "Bearer " + accessToken)
                                .url(url)
                                .build()
                        client.newCall(request).execute().body()!!.string() },
                    { jsonData ->
                        val jsonObject = JSONObject(jsonData)
                        val images = jsonObject.getJSONArray("data")
                        val lastImage = images.getJSONObject(0)
                        val username = lastImage["account_url"] as String
                        val link = lastImage["link"] as String

                        test.text = (String.format("Logged in as: %s\nLast Image: %s", username, link))
                    }
            ).execute("https://api.imgur.com/3/account/me/images")
        }
    }
}
