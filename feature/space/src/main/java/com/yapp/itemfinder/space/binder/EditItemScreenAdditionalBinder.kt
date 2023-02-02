package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemAdditional
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemAdditionalBinder
import com.yapp.itemfinder.space.edititem.EditItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditItemScreenAdditionalBinder @Inject constructor() : AddItemAdditionalBinder {
    override fun bindData(data: AddItemAdditional, viewModel: BaseViewModel) {
        when (viewModel) {
            is EditItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemAdditional, vm: EditItemViewModel) {
        item.addMemoHandler = { vm.addMemoCell() }
        item.addExpirationDateHandler = { vm.addExpirationDateCell() }
        item.addPurchaseDateHandler = { vm.addPurchaseDateCell() }
    }

}
