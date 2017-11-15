package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // TODO(sam): Fill this class to actually hold custom comments

    init {
        itemView.findViewById<TextView>(R.id.commentContent).setText("You know, it really doesn’t matter what you write as long as you’ve got a young, and beautiful, piece of text. " +
                "I was going to say something extremely rough to Lorem Ipsum, to its family, and I said to myself," +
                " \"I can't do it. I just can't do it. It's inappropriate. It's not nice.\"\n\nI know words. I have the best words." +
                " Lorem Ipsum is the single greatest threat. We are not - we are not keeping up with other websites.")
    }
}
