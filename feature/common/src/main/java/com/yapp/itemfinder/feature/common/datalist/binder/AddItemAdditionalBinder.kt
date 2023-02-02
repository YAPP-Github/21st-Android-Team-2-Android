package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemAdditional
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemAdditionalBinder {
    fun bindData(data: AddItemAdditional, viewModel: BaseViewModel)
}
