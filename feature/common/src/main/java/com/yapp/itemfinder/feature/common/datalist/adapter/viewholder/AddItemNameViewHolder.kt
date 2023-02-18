package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.databinding.AddItemNameBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemNameViewHolder(
    val binding: AddItemNameBinding
) : DataViewHolder<AddItemName>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemName) {
        super.bindData(data)
        if (data.mode == ScreenMode.EDIT_MODE) binding.itemNameEditText.setText(data.name)
        data.saveHandler = {
            data.setItemName(binding.itemNameEditText.text.toString())
        }
    }

    override fun bindViews(data: AddItemName) = Unit

}
