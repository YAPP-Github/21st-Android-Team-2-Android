package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemPurchaseDate
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemPurchaseDateBinder
import com.yapp.itemfinder.space.edititem.EditItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditItemScreenPurchaseDateBinder @Inject constructor(): AddItemPurchaseDateBinder{
    override fun bindData(data: AddItemPurchaseDate, viewModel: BaseViewModel) {
        when (viewModel) {
            is EditItemViewModel -> {
                data.openDatePickerHandler = {
                    viewModel.openPurchaseDatePicker()
                }
            }
        }
    }

}
