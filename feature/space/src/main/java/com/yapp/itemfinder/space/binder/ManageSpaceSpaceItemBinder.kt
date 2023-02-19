package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.ManageSpaceItemBinder
import com.yapp.itemfinder.space.managespace.ManageSpaceViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageSpaceSpaceItemBinder @Inject constructor() : ManageSpaceItemBinder {
    override fun bindData(data: ManageSpaceEntity, viewModel: BaseViewModel) {
        when (viewModel) {
            is ManageSpaceViewModel -> setManageSpaceViewModelHandler(data, viewModel)
        }
    }

    private fun setManageSpaceViewModelHandler(
        item: ManageSpaceEntity,
        viewModel: ManageSpaceViewModel
    ) {
        item.editSpaceDialogHandler = { data ->
            viewModel.editSpaceDialog(data as ManageSpaceEntity)
        }
        item.deleteSpaceDialogHandler = { data ->
            val space = data as ManageSpaceEntity
            viewModel.openDeleteSpaceDialog(spaceId = space.id, spaceName = space.name)
        }
    }
}
