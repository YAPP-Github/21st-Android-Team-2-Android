package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemMemo
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemMemoBinder {
    fun bindDate(data: AddItemMemo, viewModel: BaseViewModel)
}
