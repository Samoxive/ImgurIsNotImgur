package com.imgurisnotimgur

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.data.ImageRecord
import com.imgurisnotimgur.entities.Image
import com.imgurisnotimgur.entities.SubredditImage
import kotlinx.android.synthetic.main.activity_subreddit_image_detail.*

class SubredditImageDetail : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    var imageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit_image_detail)

        val image = intent.getParcelableExtra<SubredditImage>("image")

        if (image != null) {
            subredditDetailImageTitle.text = image.title
            subredditDetailImageTime.text = ImageUtils.getTimeString(this, image.createdAt)
        }

        imageId = image.id
        loaderManager.initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = CursorLoader(
            this,
            ImageRecord.buildImageUri(imageId!!),
            ImageRecord.COLUMNS,
            null,
            null,
            null)

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        AsyncAction({
            val imageFile = Imgur.getImageFile(contentResolver, data, imageId!!)
            ImageUtils.getScaledDownBitmap(imageFile)
        }, { subredditDetailImage.setImageBitmap(it) })
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {}
}
