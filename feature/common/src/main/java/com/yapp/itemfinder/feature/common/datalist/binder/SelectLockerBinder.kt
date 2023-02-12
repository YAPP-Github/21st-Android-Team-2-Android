package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.SelectLockerEntity
import com.yapp.itemfinder.feature.common.BaseViewModel

interface SelectLockerBinder {

    fun bindData(data: SelectLockerEntity, viewModel: BaseViewModel)

}
