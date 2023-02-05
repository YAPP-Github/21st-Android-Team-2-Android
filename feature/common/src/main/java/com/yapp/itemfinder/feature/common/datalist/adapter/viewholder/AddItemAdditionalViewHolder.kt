package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemAdditional
import com.yapp.itemfinder.feature.common.databinding.AddItemAdditionalBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class AddItemAdditionalViewHolder(
    val binding: AddItemAdditionalBinding
) : DataViewHolder<AddItemAdditional>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemAdditional) {
        binding.apply {
            addMemoButton.setOnClickListener { data.addMemo() }
            addExpirationDateButton.setOnClickListener { data.addExpirationDate() }
            addPurchaseDateButton.setOnClickListener { data.addPurchaseDate() }

            if (data.hasMemo) addMemoButton.gone()
            else addMemoButton.visible()

            if (data.hasExpirationDate) addExpirationDateButton.gone()
            else addExpirationDateButton.visible()

            if (data.hasPurchaseDate) addPurchaseDateButton.gone()
            else addPurchaseDateButton.visible()
        }
    }

}
