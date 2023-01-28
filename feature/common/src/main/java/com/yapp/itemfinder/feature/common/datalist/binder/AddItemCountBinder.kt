package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemCount
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemCountBinder {
    fun bindData(data: AddItemCount, viewModel: BaseViewModel)
}
