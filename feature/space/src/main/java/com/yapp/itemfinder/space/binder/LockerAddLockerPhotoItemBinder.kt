package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddLockerPhoto
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerPhotoItemBinder
import com.yapp.itemfinder.space.addlocker.AddLockerViewModel
import javax.inject.Inject

class LockerAddLockerPhotoItemBinder @Inject constructor() : AddLockerPhotoItemBinder {
    override fun bindData(data: AddLockerPhoto, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddLockerViewModel -> setAddLockerViewModelHandler(data, viewModel)
        }
    }

    private fun setAddLockerViewModelHandler(
        item: AddLockerPhoto,
        viewModel: AddLockerViewModel
    ) {
        item.uploadImageHandler = {
            viewModel.addImage()
        }
    }
}
