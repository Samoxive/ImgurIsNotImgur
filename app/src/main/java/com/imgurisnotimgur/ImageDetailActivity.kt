package com.imgurisnotimgur

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import com.imgurisnotimgur.data.CommentRecord
import com.imgurisnotimgur.data.ImageRecord
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    val COMMENT_LOADER = 42
    val IMAGE_LOADER = 33
    var _image: Image? = null
    var imageId: String? = null
    var commentAdapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val startIntent = intent
        val image = startIntent.getParcelableExtra<Image>("image")
        commentAdapter = CommentAdapter(arrayOf(), this)

        detailImageTitle.text = image.title
        detailImageAuthor.text = image.username
        detailImageDate.text = ImageUtils.getTimeString(this, image.createdAt)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        StaggeredGridLayoutManager(1, 1).isAutoMeasureEnabled
        layoutManager.isAutoMeasureEnabled = false
        commentsView.layoutManager = layoutManager
        commentsView.adapter = commentAdapter

        _image = image
        imageId = if (image.isAlbum) { image.albumId } else { image.id }
        loaderManager.initLoader(COMMENT_LOADER, null, this)
        loaderManager.initLoader(IMAGE_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            COMMENT_LOADER -> CursorLoader(this, CommentRecord.buildImageCommentsUri(imageId!!), CommentRecord.COLUMNS, null, null, "${CommentRecord.COLUMN_POINTS} DESC")
            IMAGE_LOADER -> CursorLoader(this, ImageRecord.buildImageUri(_image!!.id), ImageRecord.COLUMNS, null, null, null)
            else -> throw Exception("Invalid Loader id!")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (loader == null) {
            return
        }

        if (loader.id == COMMENT_LOADER) {
            AsyncAction({ Imgur.getComments(contentResolver, data, _image!!) }, { commentAdapter!!.items = it })
        } else if (loader.id == IMAGE_LOADER) {
            AsyncAction({
                val imageFile = Imgur.getImageFile(contentResolver, data, _image!!.id)
                ImageUtils.getScaledDownBitmap(imageFile)
            }, { detailImageView.setImageBitmap(it) })
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {}
}
