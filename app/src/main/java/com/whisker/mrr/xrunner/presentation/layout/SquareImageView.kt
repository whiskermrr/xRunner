package com.whisker.mrr.xrunner.presentation.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class SquareImageView : ImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrSet: AttributeSet?) : super(context, attrSet)
    constructor(context: Context, attrSet: AttributeSet?, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}