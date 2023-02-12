package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemLocation
import com.yapp.itemfinder.feature.common.databinding.AddItemLocationBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class AddItemLocationViewHolder(
    val binding: AddItemLocationBinding
) : DataViewHolder<AddItemLocation>(binding) {
    override fun reset() = Unit

    override fun bindData(data: AddItemLocation) {
        super.bindData(data)
        with(binding) {
            if (data.spaceName == "" && data.lockerName == "") {
                itemLocationGroup.gone()
                arrowForward.gone()
                itemLocationHintTextView.visible()
            } else {
                itemLocationHintTextView.gone()
                itemLocationGroup.visible()
                arrowForward.visible()
                itemLocationSpaceTextView.text = data.spaceName
                itemLocationLockerTextView.text = data.lockerName
            }
        }
    }

    override fun bindViews(data: AddItemLocation) {
        binding.root.setOnClickListener {
            data.runMoveSelectSpace()
        }
    }
}
