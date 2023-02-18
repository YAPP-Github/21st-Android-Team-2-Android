package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.LockerItemBinder
import com.yapp.itemfinder.space.LockerListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerLockerItemBinder @Inject constructor() : LockerItemBinder {
    override fun bindData(data: LockerEntity, viewModel: BaseViewModel) {
        when (viewModel) {
            is LockerListViewModel -> setLockerListViewModelHandler(data, viewModel)
        }
    }

    private fun setLockerListViewModelHandler(
        lockerItem: LockerEntity,
        viewModel: LockerListViewModel
    ) {
        lockerItem.deleteHandler = { data ->
            viewModel.deleteItem(data as LockerEntity)
        }
        lockerItem.editHandler = { data ->
            viewModel.editItem(data as LockerEntity)
        }
        lockerItem.moveLockerDetailHandler = { data ->
            viewModel.moveLockerDetail(data as LockerEntity)
        }
    }

}
