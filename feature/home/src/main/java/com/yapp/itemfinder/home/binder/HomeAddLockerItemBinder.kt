package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerItemBinder
import com.yapp.itemfinder.home.lockerlist.LockerListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeAddLockerItemBinder @Inject constructor() : AddLockerItemBinder {
    override fun bindData(data: AddLocker, viewModel: BaseViewModel) {
        when (viewModel){
            is LockerListViewModel -> setLockerListViewModelHandler(data, viewModel)
        }
    }

    private fun setLockerListViewModelHandler(item: AddLocker, viewModel: LockerListViewModel) {
        item.addLockerHandler = { data ->
            viewModel.addItem()
        }
    }
}