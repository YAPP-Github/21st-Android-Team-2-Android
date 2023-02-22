package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemTags
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemTagsBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenTagsBinder @Inject constructor() : AddItemTagsBinder {

    override fun bindData(data: AddItemTags, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setAddItemViewModelHandler(data, viewModel)
        }
    }

    private fun setAddItemViewModelHandler(item: AddItemTags, viewModel: AddItemViewModel) {
        item.addTagHandler = {
            viewModel.moveAddTag()
        }
    }
}
