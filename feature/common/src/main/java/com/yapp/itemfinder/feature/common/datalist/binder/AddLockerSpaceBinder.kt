package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddLockerSpace
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddLockerSpaceBinder {
    fun bindData(data: AddLockerSpace, viewModel: BaseViewModel)
}
