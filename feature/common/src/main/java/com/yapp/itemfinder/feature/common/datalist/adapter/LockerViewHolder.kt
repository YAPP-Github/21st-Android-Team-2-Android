package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.databinding.LockerItemBinding

class LockerViewHolder(
    val binding: LockerItemBinding
) : DataViewHolder<Locker>(binding) {
    override fun reset() {
    }

    override fun bindData(data: Locker) {
        super.bindData(data)
        binding.lockerItemTextView.text = data.name
    }

    override fun bindViews(data: Locker) {
        binding.spinnerImageButton.setOnClickListener {

        }
    }
}
