package com.imgurisnotimgur

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class GalleryAdapter(items: IntArray, val activity: Activity) : RecyclerView.Adapter<GalleryViewHolder>() {
    var items: IntArray = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_list_item, parent, false)
        return GalleryViewHolder(view, this)
        //TODO(ozan): ADD LISTENER ATTACHED with return GVH(v).onClick fun
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        //TODO(sam): Move this hack to some place that is more fit
        // Android appearently stores images in memory uncompressed, which means images consume
        // x * y * 4 bytes of memory, which is around 8 mb for an hd image. So we downscaled
        // images here for a smoother experience for the user
        val original = BitmapFactory.decodeResource(activity.resources, items[position])

        // Horrible, horrible hack. I investigated ways to fetch the imageview's dimensions
        // but all of them involve listening to changes in runtime, which makes implementation difficult
        // In the future, REPLACE this ungodly mess.
        val width = 600
        val scaleFactor = width.toFloat() / original.width.toFloat()
        val height = (scaleFactor * original.height.toFloat()).toInt()
        val scaled = Bitmap.createScaledBitmap(original, width, height, true)
        holder.bind(scaled)
    }

    override fun getItemCount(): Int = items.size

    fun onItemClick(position: Int) {
        val intent = Intent(activity, ImageDetailActivity::class.java)
        intent.putExtra("imageResId", items[position])
        activity.startActivity(intent)
    }
}