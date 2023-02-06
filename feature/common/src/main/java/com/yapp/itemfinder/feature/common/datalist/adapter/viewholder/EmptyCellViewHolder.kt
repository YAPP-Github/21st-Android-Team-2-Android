package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.widget.LinearLayout
import com.yapp.itemfinder.domain.model.EmptyCellItem
import com.yapp.itemfinder.feature.common.databinding.EmptyCellItemBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
import com.yapp.itemfinder.feature.common.extension.dpToPx

class EmptyCellViewHolder(
    val binding: EmptyCellItemBinding
): DataViewHolder<EmptyCellItem>(binding ) {
    override fun reset() {
        return
    }

    override fun bindViews(data: EmptyCellItem) {
        with(binding.root){
            layoutParams = LinearLayout.LayoutParams(width, data.heightDp.dpToPx(context))
        }

    }
}
