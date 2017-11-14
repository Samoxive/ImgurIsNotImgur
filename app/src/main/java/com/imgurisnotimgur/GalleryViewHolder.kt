package com.imgurisnotimgur

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var listItemGalleryView = itemView.findViewById<ImageView>(R.id.imageView)

    fun bind(s: Bitmap) {
        listItemGalleryView.setImageBitmap(s)
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        listItemGalleryView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
        //TODO: NOT REACHABLE, NEED TO FIX
    }
}