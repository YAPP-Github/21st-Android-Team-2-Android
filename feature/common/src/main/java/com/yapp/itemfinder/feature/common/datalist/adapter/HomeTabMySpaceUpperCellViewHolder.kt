package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.CellItem
import com.yapp.itemfinder.feature.common.databinding.HometabMyspaceUpperCellItemBinding

class HomeTabMySpaceUpperCellViewHolder(
    val binding: HometabMyspaceUpperCellItemBinding
) : DataViewHolder<CellItem>(binding) {

    override fun reset() {
        //
    }

    override fun bindData(data: CellItem) {
        super.bindData(data)
    }


    override fun bindViews(data: CellItem) {
        binding.MySpaceUpperCellTEdit.setOnClickListener { data.action() }
    }
}
