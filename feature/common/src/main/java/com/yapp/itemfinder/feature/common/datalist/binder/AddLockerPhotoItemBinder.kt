package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.AddLockerPhoto
import com.yapp.itemfinder.feature.common.BaseViewModel

interface AddLockerPhotoItemBinder {
    fun bindData(data: AddLockerPhoto, viewModel: BaseViewModel)
}
