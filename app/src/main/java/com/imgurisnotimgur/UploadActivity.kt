package com.imgurisnotimgur

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.navigation_bar.*

class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadAct.isEnabled = false
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))

        cameraUpload.setOnClickListener {
            // do camera stuff
        }

        pickUpload.setOnClickListener {
            // do gallery stuff
        }
    }
}
