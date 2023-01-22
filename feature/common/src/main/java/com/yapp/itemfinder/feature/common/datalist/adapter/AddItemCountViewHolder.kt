package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemCount
import com.yapp.itemfinder.feature.common.databinding.AddItemCountBinding

class AddItemCountViewHolder(
    val binding: AddItemCountBinding
) : DataViewHolder<AddItemCount>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemCount) {
        binding.countTextView.text = data.count.toString()
        binding.countPlusButton.setOnClickListener { data.plusCount() }
        binding.countMinusButton.setOnClickListener { data.minusCount() }
    }

}