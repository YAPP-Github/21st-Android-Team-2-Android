package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddLockerSpace
import com.yapp.itemfinder.feature.common.databinding.AddLockerSpaceBinding

class AddLockerSpaceViewHolder(
    val binding: AddLockerSpaceBinding
) : DataViewHolder<AddLockerSpace>(binding){
    override fun reset() {
        return
    }

    override fun bindViews(data: AddLockerSpace) {
        binding.selectSpaceButton.text = data.name
        binding.selectSpaceButton.setOnClickListener {
            data.selectSpace()
        }
    }

}
