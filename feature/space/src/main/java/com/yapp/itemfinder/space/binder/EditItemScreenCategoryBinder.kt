package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemCategory
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCategoryBinder
import com.yapp.itemfinder.space.edititem.EditItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditItemScreenCategoryBinder @Inject constructor() :
    AddItemCategoryBinder {
    override fun bindData(data: AddItemCategory, viewModel: BaseViewModel) {
        when (viewModel) {
            is EditItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemCategory, viewModel: EditItemViewModel) {
        item.selectCategoryHandler = {
            viewModel.openSelectCategoryDialog()
        }
    }
}
