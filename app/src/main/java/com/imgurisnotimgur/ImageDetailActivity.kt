package com.imgurisnotimgur

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val startIntent = intent
        val imageId = startIntent.getIntExtra("imageResId", 0)
        detailImageView.setImageResource(imageId)

        commentsView.adapter = CommentAdapter(intArrayOf(0, 0, 0))
    }
}
