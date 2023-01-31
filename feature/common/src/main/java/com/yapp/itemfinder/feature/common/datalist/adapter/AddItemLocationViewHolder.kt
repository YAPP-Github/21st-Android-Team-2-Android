package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemLocation
import com.yapp.itemfinder.feature.common.databinding.AddItemLocationBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class AddItemLocationViewHolder(
    val binding: AddItemLocationBinding
) : DataViewHolder<AddItemLocation>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemLocation) {
        if (data.spaceName == "" && data.lockerName == "") {
            binding.itemLocationGroup.gone()
            binding.arrowForward.gone()
            binding.itemLocationHintTextView.visible()
        } else {
            binding.itemLocationHintTextView.gone()
            binding.itemLocationGroup.visible()
            binding.arrowForward.visible()
        }
    }
}
