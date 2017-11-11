package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_no_auth.*

class NoAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_auth)
        title = "Imgur Authentication"

        authButton.setOnClickListener {
            val intent = Intent(this@NoAuthActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
