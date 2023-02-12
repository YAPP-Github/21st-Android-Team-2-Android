package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemPurchaseDate
import com.yapp.itemfinder.feature.common.databinding.AddItemPurchaseDateBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemPurchaseDateViewHolder(
    val binding: AddItemPurchaseDateBinding
) : DataViewHolder<AddItemPurchaseDate>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemPurchaseDate) {
        super.bindData(data)
        binding.purchaseDateTextView.text = data.purchaseDate
    }

    override fun bindViews(data: AddItemPurchaseDate) {
        binding.purchaseDateTextView.setOnClickListener { data.openDatePicker() }
    }
}
