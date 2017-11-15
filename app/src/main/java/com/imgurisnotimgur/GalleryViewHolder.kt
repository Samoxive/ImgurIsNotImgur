package com.imgurisnotimgur

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

class GalleryViewHolder(itemView: View, val adapter: GalleryAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var listItemGalleryView = itemView.findViewById<ImageView>(R.id.imageView)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(s: Bitmap) {
        listItemGalleryView.setImageBitmap(s)
    }

    override fun onClick(v: View) {
        val position = adapterPosition
        adapter.onItemClick(position)
    }
}