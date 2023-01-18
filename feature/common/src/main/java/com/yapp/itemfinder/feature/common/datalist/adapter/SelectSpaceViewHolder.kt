package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.feature.common.databinding.SelectSpaceBinding

class SelectSpaceViewHolder(
    val binding: SelectSpaceBinding
) : DataViewHolder<SelectSpace>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: SelectSpace) {
        binding.spaceName.text = data.name
    }

}
