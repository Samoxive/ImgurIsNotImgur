package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.imgurisnotimgur.entities.SubredditImage

class SubredditGalleryAdapter(items: Array<SubredditImage>): RecyclerView.Adapter<SubredditGalleryViewHolder>() {
    var items: Array<SubredditImage> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditGalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.subreddit_gallery_list_item, parent, false)
        return SubredditGalleryViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: SubredditGalleryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun onItemClick(position: Int) {

    }
}