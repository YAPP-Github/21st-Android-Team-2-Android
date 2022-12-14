package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.databinding.AddLockerBinding

class AddLockerViewHolder(
    val binding: AddLockerBinding
) : DataViewHolder<AddLocker>(binding){
    override fun reset() {
        return
    }

    override fun bindViews(data: AddLocker) {
        binding.addIconCardView.setOnClickListener {
            data.addLocker()
        }
    }

}
