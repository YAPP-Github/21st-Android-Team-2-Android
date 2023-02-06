package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemSelectSpaceEntity
import com.yapp.itemfinder.feature.common.databinding.ViewholderAddItemSelectSpaceBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemSelectSpaceViewHolder(
    private val binding: ViewholderAddItemSelectSpaceBinding
) : DataViewHolder<AddItemSelectSpaceEntity>(binding) {

    override fun reset() = Unit

    override fun bindData(data: AddItemSelectSpaceEntity) {
        super.bindData(data)
        with(binding) {
            spaceNameTextView.text = data.name
        }
    }

    override fun bindViews(data: AddItemSelectSpaceEntity) {
        binding.root.setOnClickListener {
            data.runSelectSpace()
        }
    }

}
