package com.imgurisnotimgur

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ScrollView
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Comment
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val startIntent = intent
        val image = startIntent.getParcelableExtra<Image>("image")
        val commentAdapter = CommentAdapter(arrayOf(), this)

        detailImageTitle.text = image.title
        detailImageAuthor.text = image.username
        detailImageDate.text = image.createdAt.toString()

        AsyncAction({ ImgurApi.getImageFile(image.id) }, { file ->
            val bitmap = ImageUtils.getScaledDownBitmap(file)
            detailImageView.setImageBitmap(bitmap)
        })

        AsyncAction({ ImgurApi.getComments(image) }, { commentAdapter.items = it })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        StaggeredGridLayoutManager(1, 1).isAutoMeasureEnabled
        layoutManager.isAutoMeasureEnabled = false
        commentsView.layoutManager = layoutManager
        commentsView.adapter = commentAdapter
    }
}
