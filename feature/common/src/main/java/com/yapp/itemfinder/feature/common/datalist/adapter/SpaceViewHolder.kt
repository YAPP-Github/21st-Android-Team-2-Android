package com.yapp.itemfinder.feature.common.datalist.adapter

import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding

class SpaceViewHolder(
    val binding: SpaceItemBinding
) : DataViewHolder<SpaceItem>(binding) {

    override fun bindData(data: SpaceItem) {
        super.bindData(data)
        with(binding) {
            spaceItemName.text = data.name

            data.lockerList.run {
                getOrNull(0)?.let { Glide.with(itemView).load(it.url).into(spaceFirstImageView) }
                getOrNull(1)?.let { Glide.with(itemView).load(it.url).into(spaceSecondViewImage) }
                getOrNull(2)?.let { Glide.with(itemView).load(it.url).into(spaceThirdImageView) }
                getOrNull(3)?.let { Glide.with(itemView).load(R.drawable.arrow_forward_16).into(spaceFourthImageView) }
            }
        }
    }

    override fun bindViews(data: SpaceItem) {
        binding.root.setOnClickListener { data.goSpaceDetailPage() }
    }

    override fun reset() {

    }
}