package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class GalleryAdapter(private val items: IntArray) : RecyclerView.Adapter<GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_list_item, parent, false)
        return GalleryViewHolder(view)
        //TODO(ozan): ADD LISTENER ATTACHED with return GVH(v).onClick fun
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(R.mipmap.ic_launcher_round)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}