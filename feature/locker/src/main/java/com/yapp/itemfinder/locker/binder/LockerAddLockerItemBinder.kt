package com.yapp.itemfinder.locker.binder

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerItemBinder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerAddLockerItemBinder @Inject constructor() : AddLockerItemBinder {
    override fun bindData(data: AddLocker, viewModel: BaseViewModel) {
        when (viewModel){
            is com.yapp.itemfinder.locker.LockerListViewModel -> setLockerListViewModelHandler(data, viewModel)
        }
    }

    private fun setLockerListViewModelHandler(item: AddLocker, viewModel: com.yapp.itemfinder.locker.LockerListViewModel) {
        item.addLockerHandler = { data ->
            viewModel.addItem()
        }
    }
}
