package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemInfoRequired
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemBinder {
    fun bindData(data: AddItemInfoRequired, viewModel: BaseViewModel)
}
