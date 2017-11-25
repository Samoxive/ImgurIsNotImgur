package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.imgurisnotimgur.entities.Comment

class CommentAdapter(items: Array<Comment>): RecyclerView.Adapter<CommentViewHolder>() {
    var items: Array<Comment> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
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