package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemExpirationDate
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemExpirationDateBinder {
    fun bindData(data: AddItemExpirationDate, viewModel: BaseViewModel)
}
