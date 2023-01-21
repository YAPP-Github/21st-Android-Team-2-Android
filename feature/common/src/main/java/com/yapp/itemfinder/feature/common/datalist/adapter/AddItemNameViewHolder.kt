package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.feature.common.databinding.AddItemNameBinding

class AddItemNameViewHolder(
    val binding: AddItemNameBinding
) : DataViewHolder<AddItemName>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemName) {
        if (data.name != "") binding.itemNameEditText.setText(data.name)
    }

}
