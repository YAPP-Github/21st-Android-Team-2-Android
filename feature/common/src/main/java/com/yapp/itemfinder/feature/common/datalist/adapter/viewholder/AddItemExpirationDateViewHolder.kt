package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemExpirationDate
import com.yapp.itemfinder.feature.common.databinding.AddItemExpirationDateBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemExpirationDateViewHolder(
    val binding: AddItemExpirationDateBinding
) : DataViewHolder<AddItemExpirationDate>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemExpirationDate) {
        super.bindData(data)
        binding.expirationDateTextView.text = data.expirationDate
    }

    override fun bindViews(data: AddItemExpirationDate) {
        binding.expirationDateTextView.setOnClickListener { data.openDatePicker() }
    }
}
