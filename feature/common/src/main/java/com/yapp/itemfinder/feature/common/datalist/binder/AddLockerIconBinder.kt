package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.LockerIcons
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddLockerIconBinder {
    fun bindData(data: LockerIcons, viewModel: BaseViewModel)
}
