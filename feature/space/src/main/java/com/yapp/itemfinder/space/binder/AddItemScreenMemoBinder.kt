package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemMemo
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemMemoBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenMemoBinder @Inject constructor() : AddItemMemoBinder {
    override fun bindDate(data: AddItemMemo, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> {
                data.enterHandler = {
                    viewModel.setMemo(it)
                }
            }
        }
    }
}
