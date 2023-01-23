package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemMemo
import com.yapp.itemfinder.feature.common.databinding.AddItemMemoBinding

class AddItemMemoViewHolder(
    val binding: AddItemMemoBinding
) : DataViewHolder<AddItemMemo>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemMemo) {
        super.bindData(data)
        binding.memoEditText.setText(data.memo)
    }

    override fun bindViews(data: AddItemMemo) {
        return
    }
}
