package com.imgurisnotimgur

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import com.imgurisnotimgur.entities.SubredditImage
import kotlinx.android.synthetic.main.activity_subreddit_image_detail.*

class SubredditImageDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit_image_detail)

        val image = intent.getParcelableExtra<SubredditImage>("image")

        if (image != null) {
            subredditDetailImageTitle.text = image.title
            subredditDetailImageTime.text = ImageUtils.getTimeString(this, image.createdAt)
            AsyncAction({ Imgur.getImageFile(contentResolver, image.id) }, { file ->
                val bitmap = ImageUtils.getScaledDownBitmap(file)
                subredditDetailImage.setImageBitmap(bitmap)
            })
        }
    }
}
