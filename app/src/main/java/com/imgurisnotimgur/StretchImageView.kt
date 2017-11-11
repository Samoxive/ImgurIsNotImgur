package com.imgurisnotimgur

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

// Refer to this blog post to have an idea of how this works
// https://www.ryadel.com/en/android-proportionally-stretch-imageview-fit-whole-screen-width-maintaining-aspect-ratio/
class StretchImageView : ImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            val w = MeasureSpec.getSize(widthMeasureSpec)
            val h = w * drawable.intrinsicHeight / drawable.intrinsicHeight
            setMeasuredDimension(w, h)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}