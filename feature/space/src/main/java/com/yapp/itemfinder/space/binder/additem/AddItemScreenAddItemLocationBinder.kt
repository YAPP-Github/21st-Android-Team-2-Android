package com.yapp.itemfinder.space.binder.additem

import com.yapp.itemfinder.domain.model.AddItemLocation
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemLocationBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenAddItemLocationBinder @Inject constructor() : AddItemLocationBinder {

    override fun bindData(data: AddItemLocation, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setMoveSelectSpaceHandler(data, viewModel)
        }
    }

    private fun setMoveSelectSpaceHandler(data: AddItemLocation, viewModel: AddItemViewModel) {
        data.moveSelectSpaceHandler = {
            viewModel.moveSelectSpace()
        }
    }

}
