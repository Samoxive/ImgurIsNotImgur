package com.imgurisnotimgur

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.imgurisnotimgur.entities.Image

class GalleryViewHolder(itemView: View, val adapter: GalleryAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val listItemGalleryView = itemView.findViewById<ImageView>(R.id.imageView)
    private val titleText = itemView.findViewById<TextView>(R.id.imageTitle)
    private val imageUpvotesText = itemView.findViewById<TextView>(R.id.imageUpvotes)
    private val imageAuthorText = itemView.findViewById<TextView>(R.id.imageAuthor)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(imagePair: Pair<Image, ByteArray>) {
        val (image, imageFile) = imagePair
        titleText.text = image.title
        imageUpvotesText.text = "${image.points} ${adapter.activity.getText(R.string.points)}"
        imageAuthorText.text = image.username
        val bitmap = BitmapFactory.decodeByteArray(imageFile, 0, imageFile.size)
        listItemGalleryView.setImageBitmap(bitmap)
    }

    override fun onClick(v: View) {
        val position = adapterPosition
        adapter.onItemClick(position)
    }
}