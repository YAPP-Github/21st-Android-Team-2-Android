package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddLockerItemBinder {
    fun bindData(data: AddLocker, viewModel: BaseViewModel)
}
