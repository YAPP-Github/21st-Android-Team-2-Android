package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.ManageSpaceItemBinder
import com.yapp.itemfinder.space.managespace.ManageSpaceViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageSpaceSpaceItemBinder @Inject constructor(): ManageSpaceItemBinder {
    override fun bindData(data: ManageSpaceItem, viewModel: BaseViewModel) {
        when (viewModel) {
            is ManageSpaceViewModel -> setManageSpaceViewModelHandler(data, viewModel)
        }
    }

    private fun setManageSpaceViewModelHandler(item: ManageSpaceItem, viewModel: ManageSpaceViewModel){
        item.editSpaceDialogHandler = { data ->
            viewModel.editItem(data as ManageSpaceItem)
        }
        item.deleteSpaceDialogHandler = { data ->
            viewModel.deleteItem(data as ManageSpaceItem)
        }
    }
}
