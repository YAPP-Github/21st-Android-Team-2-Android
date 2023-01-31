package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemNameBinder {
    fun bindData(data: AddItemName, viewModel: BaseViewModel)
}
