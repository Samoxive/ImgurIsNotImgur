package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val s = getSharedPreferences("secret", Context.MODE_PRIVATE).getString("refreshToken", null)

        if (s == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)
        }

    }
}
