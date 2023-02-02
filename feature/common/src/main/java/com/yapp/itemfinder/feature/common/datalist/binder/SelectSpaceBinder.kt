package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.feature.common.BaseViewModel

interface SelectSpaceBinder {
    fun bindData(data: SelectSpace, viewModel: BaseViewModel)
}
