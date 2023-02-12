package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemLocation
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemLocationBinder {
    fun bindData(data: AddItemLocation, viewModel: BaseViewModel)
}
