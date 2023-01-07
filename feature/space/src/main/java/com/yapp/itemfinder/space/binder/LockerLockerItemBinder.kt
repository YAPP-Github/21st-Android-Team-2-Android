package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.LockerItemBinder
import com.yapp.itemfinder.space.LockerListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerLockerItemBinder @Inject constructor(): LockerItemBinder {
    override fun bindData(data: Locker, viewModel: BaseViewModel) {
        when (viewModel){
            is LockerListViewModel -> setLockerListViewModelHandler(data, viewModel)
        }
    }

    private fun setLockerListViewModelHandler(lockerItem: Locker, viewModel: LockerListViewModel){
        lockerItem.deleteHandler = { data ->
            viewModel.deleteItem(data as Locker)
        }
        lockerItem.editHandler = { data ->

        }
    }

}
