package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemNameBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenNameBinder @Inject constructor() : AddItemNameBinder {
    override fun bindData(data: AddItemName, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> {
                data.enterHandler = {
                    viewModel.setName(it)
                }
            }
        }
    }
}
