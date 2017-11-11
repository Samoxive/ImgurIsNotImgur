package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(sam): Revert this back to a main activity, ideally we should launch the gallery activity if there isn't a problem with the tokens
        setContentView(R.layout.activity_main) //I WAS JUST TESTING THE GALLERY VIEW NAMED ACTIVITY GALLERY. DON'T MIND ME.

        val preferences = getSharedPreferences("secret", Context.MODE_PRIVATE)
        val accessToken = preferences.getString("accessToken", null)

        val intent = if (accessToken == null) {
            Intent(this@MainActivity, NoAuthActivity::class.java)
        }  else {
            Intent(this, SubredditActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
