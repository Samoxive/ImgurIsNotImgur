package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_gallery.*

class MainActivity : AppCompatActivity() {
    internal val itemgibicekpanpa = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery) //I WAS JUST TESTING THE GALLERY VIEW NAMED ACTIVITY GALLERY. DON'T MIND ME.

        val preferences = getSharedPreferences("secret", Context.MODE_PRIVATE)
        val accessToken = preferences.getString("accessToken", null)
        val client = OkHttpClient()

        if (accessToken == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)
        }  else {
            val gridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rv_gallery.layoutManager = gridLayoutManager
            val galleryAdapter = GalleryAdapter(9, itemgibicekpanpa)
            rv_gallery.adapter = galleryAdapter
        }
        AsyncAction<Unit, Unit>({ Thread.sleep(3000) }, { //test.text = "OI MATE LUL\n" + accessToken
        }).execute(Unit) // dude I removed text thing while you push that code, sorry. But good news is I added 3x3 gallery thing \o/
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

                        //test.text = (String.format("Logged in as: %s\nLast Image: %s", username, link))
                    }
            ).execute("https://api.imgur.com/3/account/me/images")
    }
}
