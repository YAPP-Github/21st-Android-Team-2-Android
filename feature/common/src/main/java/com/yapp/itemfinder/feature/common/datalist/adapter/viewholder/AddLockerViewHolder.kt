package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.databinding.AddLockerBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

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
