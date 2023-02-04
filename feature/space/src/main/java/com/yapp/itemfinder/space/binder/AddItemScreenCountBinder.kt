package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemCount
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCountBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenCountBinder @Inject constructor() : AddItemCountBinder {
    override fun bindData(data: AddItemCount, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemCount, vm: AddItemViewModel) {
        item.plusHandler = {
            if (item.count < 99) vm.countPlusOne()
        }
        item.minusHandler = {
            if (item.count > 1) vm.countMinusOne()
        }
    }
}
