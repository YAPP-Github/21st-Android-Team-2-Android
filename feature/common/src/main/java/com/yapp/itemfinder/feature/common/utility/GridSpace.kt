package com.yapp.itemfinder.feature.common.utility

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.yapp.itemfinder.feature.common.extension.dpToPx
import com.yapp.itemfinder.feature.common.extension.isOdd
import com.yapp.itemfinder.feature.common.extension.size

//https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
class SpaceItemDecoration(
    private val range: IntRange,
    private val bottomFullSpacingDp: Int,
    private val horizontalHalfSpacingDp: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view) // item position
        if (position  !in range) return

        super.getItemOffsets(outRect, view, parent, state)


        val bottomFullSpacing = bottomFullSpacingDp.dpToPx(view.context)
        val horizontalHalfSpacing = horizontalHalfSpacingDp.dpToPx(view.context)

        if (range.first % 2 == 1){ // 짝수 번째 자리에서 시작하는 경우
            if (position % 2 == 1){ // 왼쪽
                outRect.right =horizontalHalfSpacing
            }else{ // 오른쪽
                outRect.left = horizontalHalfSpacing
            }

        }else{
            if (position % 2 == 0){ // 왼쪽
                outRect.right =horizontalHalfSpacing
            }else{ // 오른쪽
                outRect.left = horizontalHalfSpacing
            }
        }

        if (range.size().isOdd()){
            if (position != range.last){
                outRect.bottom = bottomFullSpacing
            }
        }else{
            if (position !in range.last()-1 .. range.last())
                outRect.bottom = bottomFullSpacing
        }


//        val column = position % spanCount

//        if (includeEdge) {
//            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount
//            outRect.right = (column + 1) * horizontalSpacing / spanCount
//
//            if (position > spanCount) { // top edge
//                outRect.top = verticalSpacing
//            }
//            outRect.bottom = verticalSpacing // item bottom
//        } else {
//            outRect.left =
//                column * horizontalSpacing / spanCount // column * ((1f / spanCount) * horizontalSpacing)
//            outRect.right =
//                horizontalSpacing - (column + 1) * horizontalSpacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            if (position >= spanCount) {
//                outRect.top = verticalSpacing // item top
//            }
//        }
    }
}
