package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemCount
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCountBinder
import com.yapp.itemfinder.space.edititem.EditItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditItemScreenCountBinder @Inject constructor() : AddItemCountBinder {
    override fun bindData(data: AddItemCount, viewModel: BaseViewModel) {
        when (viewModel) {
            is EditItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemCount, vm: EditItemViewModel) {
        item.plusHandler = {
            vm.countPlusOne()
        }
        item.minusHandler = {
            vm.countMinusOne()
        }
    }
}
