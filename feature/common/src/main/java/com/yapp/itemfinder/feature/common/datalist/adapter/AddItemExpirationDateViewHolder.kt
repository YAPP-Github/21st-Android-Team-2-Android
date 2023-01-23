package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemExpirationDate
import com.yapp.itemfinder.feature.common.databinding.AddItemExpirationDateBinding

class AddItemExpirationDateViewHolder(
    val binding: AddItemExpirationDateBinding
) : DataViewHolder<AddItemExpirationDate>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemExpirationDate) {
        binding.expirationDateTextView.setOnClickListener { data.openDatePicker() }
        binding.expirationDateTextView.text = data.expirationDate
    }
}
