package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemCategory
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCategoryBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenCategoryBinder @Inject constructor() :
    AddItemCategoryBinder {
    override fun bindData(data: AddItemCategory, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemCategory, viewModel: AddItemViewModel) {
        item.selectCategoryHandler = {
            viewModel.openSelectCategoryDialog()
        }
    }
}
