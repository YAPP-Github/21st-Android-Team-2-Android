package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.SideEffect
import com.yapp.itemfinder.feature.common.State

interface SpaceItemBinder{
    fun bindData(data: SpaceItem, viewModel:BaseViewModel)
}