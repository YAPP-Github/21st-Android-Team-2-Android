package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemMemo
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.databinding.AddItemMemoBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemMemoViewHolder(
    val binding: AddItemMemoBinding
) : DataViewHolder<AddItemMemo>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemMemo) {
        super.bindData(data)
        if (data.mode == ScreenMode.EDIT_MODE) binding.memoEditText.setText(data.memo)
        data.saveHandler = {
            data.setItemMemo(binding.memoEditText.text.toString())
        }
    }

    override fun bindViews(data: AddItemMemo) = Unit
}
