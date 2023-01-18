package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.BaseViewModel

interface LockerItemBinder {
    fun bindData(data: LockerEntity, viewModel: BaseViewModel)
}
