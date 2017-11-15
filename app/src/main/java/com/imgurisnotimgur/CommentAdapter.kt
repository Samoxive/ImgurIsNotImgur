package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class CommentAdapter(val items: IntArray): RecyclerView.Adapter<CommentViewHolder>() {
    override fun onBindViewHolder(holder: CommentViewHolder?, position: Int) {
        // TODO(sam): Fill this to actually attach custom comments
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_list_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}