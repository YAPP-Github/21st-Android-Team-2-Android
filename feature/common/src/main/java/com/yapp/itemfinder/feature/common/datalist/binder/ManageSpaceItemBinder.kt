package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.feature.common.BaseViewModel

interface ManageSpaceItemBinder {
    fun bindData(data: ManageSpaceEntity, viewModel: BaseViewModel)
}
