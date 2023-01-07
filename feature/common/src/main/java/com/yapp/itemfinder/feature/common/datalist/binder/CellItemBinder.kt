package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.MySpaceUpperCellItem
import com.yapp.itemfinder.feature.common.BaseViewModel

interface CellItemBinder{
    fun bindData(data: MySpaceUpperCellItem, viewModel:BaseViewModel)
}
