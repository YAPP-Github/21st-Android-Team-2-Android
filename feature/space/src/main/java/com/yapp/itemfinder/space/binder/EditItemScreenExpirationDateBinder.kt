package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemExpirationDate
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemExpirationDateBinder
import com.yapp.itemfinder.space.edititem.EditItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditItemScreenExpirationDateBinder @Inject constructor() : AddItemExpirationDateBinder {
    override fun bindData(data: AddItemExpirationDate, viewModel: BaseViewModel) {
        when (viewModel) {
            is EditItemViewModel -> {
                data.openDatePickerHandler = {
                    viewModel.openExpirationDatePicker()
                }
            }
        }
    }
}
