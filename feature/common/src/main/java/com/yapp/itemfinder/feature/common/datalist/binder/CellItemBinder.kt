package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.CellItem
import com.yapp.itemfinder.feature.common.BaseViewModel

interface CellItemBinder{
    fun bindData(data: CellItem, viewModel:BaseViewModel)
}
