package com.yapp.itemfinder.feature.common.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ImagesItemDecoration(private val marginBetween: Int) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0){
            outRect.right = marginBetween
            outRect.left = 0
        }
        else if (position == state.itemCount - 1) { // 마지막 부분은 마진을 주지 않습니다.
            outRect.right = 0
            outRect.left = marginBetween
        }else{
            outRect.right = marginBetween
            outRect.left = marginBetween
        }
    }
}
