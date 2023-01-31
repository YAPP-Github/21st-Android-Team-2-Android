package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemCategory
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemCategoryBinder {
    fun bindData(data: AddItemCategory, viewModel: BaseViewModel)
}
