package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding

class SpaceViewHolder(
    val binding: SpaceItemBinding
) : DataViewHolder<SpaceItem>(binding) {

    override fun bindData(data: SpaceItem) {
        super.bindData(data)
        binding.spaceItemName.text = data.name
    }
    override fun reset() {
        //TODO("Not yet implemented")
    }

    override fun bindViews(data: SpaceItem) {
        binding.root.setOnClickListener { data.goSpaceDetailPage() }
    }
}