package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.imgurisnotimgur.entities.Comment

class CommentViewHolder(itemView: View, val adapter: CommentAdapter) : RecyclerView.ViewHolder(itemView) {
    val author = itemView.findViewById<TextView>(R.id.commentAuthor)
    val time = itemView.findViewById<TextView>(R.id.commentTime)
    val content = itemView.findViewById<TextView>(R.id.commentContent)
    val points = itemView.findViewById<TextView>(R.id.commentPoints)

    fun bind(comment: Comment) {
        val pointsString = adapter.activity.getString(R.string.points)
        author.text = comment.author
        time.text = ImageUtils.getTimeString(adapter.activity, comment.createdAt)
        content.text = comment.content
        points.text = "${comment.points} $pointsString"
    }
}
