package com.yapp.itemfinder.feature.common.utility

import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.yapp.itemfinder.feature.common.extension.dpToPx

//https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
class GridSpacing(
    private val spanCount: Int,
    private val verticalSpacingDp: Int,
    private val horizontalSpacingDp: Int,
    private val includeEdge: Boolean
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount
        val verticalSpacing = verticalSpacingDp.dpToPx(view.context)
        val horizontalSpacing = horizontalSpacingDp.dpToPx(view.context)

        if (includeEdge) {
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount
            outRect.right = (column + 1) * horizontalSpacing / spanCount

            if (position > spanCount) { // top edge
                outRect.top = verticalSpacing
            }
            outRect.bottom = verticalSpacing // item bottom
        } else {
            outRect.left =
                column * horizontalSpacing / spanCount // column * ((1f / spanCount) * horizontalSpacing)
            outRect.right =
                horizontalSpacing - (column + 1) * horizontalSpacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = verticalSpacing // item top
            }
        }
    }
}
