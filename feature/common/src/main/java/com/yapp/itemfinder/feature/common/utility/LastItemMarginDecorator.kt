package com.yapp.itemfinder.feature.common.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.yapp.itemfinder.feature.common.extension.dpToPx

class AddImagesItemDecoration(private val marginBetween: Int) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0){ // cancel버튼이 없습니다.
            outRect.right = marginBetween
        }
        else if (position == state.itemCount - 1) { // 마지막 부분은 마진을 주지 않습니다. ㅁㄴ
            outRect.right = 0
        }else{ // 마진의 절반은 cancel 버튼이 차지합니다.
            outRect.right = marginBetween / 2
        }
    }
}
