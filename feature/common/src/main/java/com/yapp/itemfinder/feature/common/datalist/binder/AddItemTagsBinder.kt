package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemTags
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemTagsBinder {
    fun bindData(data: AddItemTags, viewModel: BaseViewModel)
}
