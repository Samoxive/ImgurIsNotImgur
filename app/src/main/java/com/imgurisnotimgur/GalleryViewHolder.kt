package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

/**
 * Created by ozankaraali on 10.10.2017.
 */

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    internal var listItemGalleryView: ImageView

    init {
        listItemGalleryView = itemView.findViewById<View>(R.id.imageView) as ImageView
    }

    internal fun bind(s: Int) {
        listItemGalleryView.setImageResource(s)
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        listItemGalleryView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
        //TODO: NOT REACHABLE, NEED TO FIX
    }
}