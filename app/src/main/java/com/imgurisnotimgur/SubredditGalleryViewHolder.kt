package com.imgurisnotimgur

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.SubredditImage

class SubredditGalleryViewHolder(itemView: View, val adapter: SubredditGalleryAdapter): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var imageView = itemView.findViewById<ImageView>(R.id.subredditImageView)
    private var titleText = itemView.findViewById<TextView>(R.id.subredditImageTitle)
    init {
        itemView.setOnClickListener(this)
    }

    fun bind(subredditImage: SubredditImage) {
        titleText.text = subredditImage.title

        AsyncAction<Unit, ByteArray>({ x ->
            ImgurApi.getThumbnailFile(subredditImage.id)
        }, {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageView.setImageBitmap(bitmap)
        }).exec()
    }

    override fun onClick(view: View) {
        val position = adapterPosition
        adapter.onItemClick(position)
    }
}