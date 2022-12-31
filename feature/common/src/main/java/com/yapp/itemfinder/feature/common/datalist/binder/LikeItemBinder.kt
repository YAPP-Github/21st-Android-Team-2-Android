package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.BaseViewModel

interface LikeItemBinder {

    fun bindData(data: LikeItem, viewModel: BaseViewModel)

}