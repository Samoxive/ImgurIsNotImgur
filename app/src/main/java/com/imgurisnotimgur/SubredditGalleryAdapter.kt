package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.SubredditImage
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SubredditGalleryAdapter(items: Array<SubredditImage>, images: Array<ByteArray>): RecyclerView.Adapter<SubredditGalleryViewHolder>() {
    var images: Array<ByteArray> = images
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items: Array<SubredditImage> = items
        set(value) {
            field = value
            AsyncAction<SubredditImage, Array<ByteArray>>({ arrayOfSubredditImages ->
                val latch = CountDownLatch(arrayOfSubredditImages.size)
                val resultArray = Array(arrayOfSubredditImages.size, { byteArrayOf() })
                val pool = Executors.newFixedThreadPool(4)
                for (i in arrayOfSubredditImages.indices) {
                    pool.submit {
                        resultArray[i] = ImgurApi.getThumbnailFile(arrayOfSubredditImages[i].id)
                        latch.countDown()
                    }
                }
                latch.await()
                return@AsyncAction resultArray
            }, { imageFiles -> images = imageFiles }).exec(*value)
        }
    /*arrayOfSubredditImages.map {
        ImgurApi.getThumbnailFile(it.id)
    }.toTypedArray()*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditGalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.subreddit_gallery_list_item, parent, false)
        return SubredditGalleryViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: SubredditGalleryViewHolder, position: Int) {
        holder.bind(items[position] to images[position])
    }

    override fun getItemCount(): Int = items.size

    fun onItemClick(position: Int) {

    }
}