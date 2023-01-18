package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SelectSpaceBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class SelectSpaceViewHolder(
    val binding: SelectSpaceBinding
) : DataViewHolder<SelectSpace>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: SelectSpace) {
        binding.spaceName.text = data.name
        itemView.setOnClickListener { data.checkSelectSpace() }
        if (data.isChecked) {
            binding.spaceName.setTextColor(itemView.context.getColor(R.color.orange))
            binding.checkImage.setColorFilter(itemView.context.getColor(R.color.orange))
            binding.checkImage.visible()
        } else {
            binding.spaceName.setTextColor(itemView.context.getColor(R.color.gray_06))
            binding.checkImage.gone()
        }
    }

}
