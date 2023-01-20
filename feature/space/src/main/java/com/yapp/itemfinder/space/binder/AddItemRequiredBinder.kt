package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemInfoRequired
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemRequiredBinder @Inject constructor() : AddItemBinder {
    override fun bindData(data: AddItemInfoRequired, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemInfoRequired, viewModel: AddItemViewModel) {
        item.selectCategoryHandler = {
            viewModel.openSelectCategoryDialog()
        }
    }
}
