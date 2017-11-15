package com.imgurisnotimgur

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // TODO(sam): Fill this class to actually hold custom comments

    init {
        itemView.findViewById<TextView>(R.id.commentContent).setText("Cat ipsum dolor sit amet, hunt anything that moves or hopped up on goofballs hide when guests come over." +
                " Hide when guests come over intrigued by the shower, or stare at ceiling climb leg for stretch and use lap as chair." +
                " Hunt anything that moves flop over, leave dead animals as gifts for chase imaginary bugs, chase mice for stare at ceiling yet hopped up on goofballs.")
    }
}
