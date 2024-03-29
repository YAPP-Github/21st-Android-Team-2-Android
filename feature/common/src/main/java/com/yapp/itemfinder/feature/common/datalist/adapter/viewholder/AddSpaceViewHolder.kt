package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.feature.common.databinding.AddSpaceBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddSpaceViewHolder(
    val binding: AddSpaceBinding
) : DataViewHolder<AddSpace>(binding){
    override fun reset() {
        return
    }

    override fun bindViews(data: AddSpace) {
        binding.addSpaceCardView.setOnClickListener {
            data.addSpace()
        }
    }

}
