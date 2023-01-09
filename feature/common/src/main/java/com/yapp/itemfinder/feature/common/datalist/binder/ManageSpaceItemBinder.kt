package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.feature.common.BaseViewModel

interface ManageSpaceItemBinder {
    fun bindData(data: ManageSpaceItem, viewModel: BaseViewModel)
}
