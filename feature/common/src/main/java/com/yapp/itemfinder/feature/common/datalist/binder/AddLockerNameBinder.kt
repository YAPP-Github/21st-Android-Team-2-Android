package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddLockerNameInput
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddLockerNameBinder {
    fun bindData(data: AddLockerNameInput, viewModel: BaseViewModel)
}
