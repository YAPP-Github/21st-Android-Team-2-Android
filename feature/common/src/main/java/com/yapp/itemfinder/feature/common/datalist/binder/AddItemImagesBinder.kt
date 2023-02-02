package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemImagesBinder {
    fun bindData(data: AddItemImages, viewModel: BaseViewModel)
}
