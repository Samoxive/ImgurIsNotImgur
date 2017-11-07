package com.imgurisnotimgur;

/**
 * Created by ozankaraali on 7.11.2017.
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


class GalleryAdapter(private var mNumberItems: Int, private val items: IntArray) : RecyclerView.Adapter<GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_list_item, parent, false)
        return GalleryViewHolder(view)
        //TODO: ADD LISTENER ATTACHED with return GVH(v).onClick fun
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        //holder.bind(items[position]);
    }

    override fun getItemCount(): Int {
        return mNumberItems
    }

    fun setGalleryData(galleryData: Array<String>) {
        //items = galleryData;
        mNumberItems = galleryData.size
        notifyDataSetChanged()
    }
}