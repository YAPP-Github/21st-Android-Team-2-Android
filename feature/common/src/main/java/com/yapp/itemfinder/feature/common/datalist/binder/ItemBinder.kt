package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.BaseViewModel

interface ItemBinder {
    fun bindData(data: Item, viewModel: BaseViewModel)
}
