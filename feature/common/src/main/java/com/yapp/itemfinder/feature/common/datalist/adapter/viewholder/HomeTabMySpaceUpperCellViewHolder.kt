package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.MySpaceUpperCellItem
import com.yapp.itemfinder.feature.common.databinding.HometabMyspaceUpperCellItemBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class HomeTabMySpaceUpperCellViewHolder(
    val binding: HometabMyspaceUpperCellItemBinding
) : DataViewHolder<MySpaceUpperCellItem>(binding) {

    override fun reset() {
        return
    }

    override fun bindData(data: MySpaceUpperCellItem) {
        super.bindData(data)
        binding.MySpaceUpperCellNameTextView.text = data.title

    }


    override fun bindViews(data: MySpaceUpperCellItem) {
        binding.MySpaceUpperCellTEdit.setOnClickListener { data.runSpaceManagementDetail() }
    }
}
