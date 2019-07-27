package com.whisker.mrr.xrunner.presentation.adapters

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if(view is CardView) {
            outRect.left = space
            outRect.right = space
            outRect.top = space
            outRect.bottom = space
        }
    }
}