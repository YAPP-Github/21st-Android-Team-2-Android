package com.yapp.itemfinder.space.binder.additem.selectspace

import com.yapp.itemfinder.domain.model.AddItemSelectSpaceEntity
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemSelectSpaceBinder
import com.yapp.itemfinder.space.additem.selectspace.AddItemSelectSpaceViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemSelectSpaceScreenAddItemSelectSpaceBinder @Inject constructor() : AddItemSelectSpaceBinder {

    override fun bindData(data: AddItemSelectSpaceEntity, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemSelectSpaceViewModel -> setSelectSpaceHandler(data, viewModel)
        }
    }

    private fun setSelectSpaceHandler(data: AddItemSelectSpaceEntity, viewModel: AddItemSelectSpaceViewModel) {
        data.selectSpaceHandler = {
            viewModel.selectSpace(it)
        }
    }

}
