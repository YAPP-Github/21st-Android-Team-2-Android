package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemPurchaseDate
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemPurchaseDateBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenPurchaseDateBinder @Inject constructor(): AddItemPurchaseDateBinder{
    override fun bindData(data: AddItemPurchaseDate, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> {
                data.openDatePickerHandler = {
                    viewModel.openDatePicker()
                }
            }
        }
    }

}
