package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemPurchaseDate
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemPurchaseDateBinder {
    fun bindData(data: AddItemPurchaseDate, viewModel: BaseViewModel)
}
