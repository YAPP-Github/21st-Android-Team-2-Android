package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemImagesBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenImagesBinder @Inject constructor() : AddItemImagesBinder {
    override fun bindData(data: AddItemImages, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> {
                data.addCameraActionHandler = {
                    viewModel.startChooseImages()
                }
            }
        }
    }
}
