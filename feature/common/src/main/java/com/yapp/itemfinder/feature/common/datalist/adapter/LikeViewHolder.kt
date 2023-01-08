package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.databinding.LikeItemBinding

class LikeViewHolder(
    val binding: LikeItemBinding
) : DataViewHolder<LikeItem>(binding) {

    override fun reset() {
        return
    }

    override fun bindData(data: LikeItem) {
        super.bindData(data)
        binding.likeItemTv.text = data.name
    }

    override fun bindViews(data: LikeItem) {
        binding.likeItemTv.setOnClickListener { data.goLikeDetailPage() }
        binding.deleteBtn.setOnClickListener {
            data.deleteLikeItem()
        }
        binding.likeItemTv.setOnClickListener {
            data.updateLikeItem()
        }
    }

}
