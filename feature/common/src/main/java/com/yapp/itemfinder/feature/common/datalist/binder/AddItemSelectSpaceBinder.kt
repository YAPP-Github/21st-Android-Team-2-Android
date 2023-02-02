package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddItemSelectSpaceEntity
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddItemSelectSpaceBinder {

    fun bindData(data: AddItemSelectSpaceEntity, viewModel: BaseViewModel)

}
