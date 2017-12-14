package com.imgurisnotimgur

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import java.util.concurrent.CountDownLatch

class GalleryAdapter(items: Array<Image>, images: Array<ByteArray>, val activity: Activity) : RecyclerView.Adapter<GalleryViewHolder>() {
    var images: Array<ByteArray> = images
        @Synchronized set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items: Array<Image> = items
        @Synchronized set(value) {
            field = value
            AsyncAction({
                val size = value.size
                val latch = CountDownLatch(size)
                val resultArray = Array(size, { byteArrayOf() })
                val pool = AsyncAction.pool
                for (i in value.indices) {
                    pool.submit {
                        resultArray[i] = Imgur.getThumbnailFile(activity.contentResolver, value[i].id)
                        latch.countDown()
                    }
                }
                latch.await()
                return@AsyncAction resultArray
            }, { imageFiles -> images = imageFiles })
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_list_item, parent, false)
        return GalleryViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(items[position] to images[position])
    }

    override fun getItemCount(): Int = items.size

    fun onItemClick(position: Int) {
        val intent = Intent(activity, ImageDetailActivity::class.java)
        intent.putExtra("image", items[position])
        activity.startActivity(intent)
    }
}