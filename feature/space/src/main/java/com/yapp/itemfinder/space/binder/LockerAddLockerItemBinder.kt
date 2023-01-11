package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerItemBinder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerAddLockerItemBinder @Inject constructor() : AddLockerItemBinder {
    override fun bindData(data: AddLocker, viewModel: BaseViewModel) {
        when (viewModel) {
            is com.yapp.itemfinder.space.LockerListViewModel -> setLockerListViewModelHandler(
                data,
                viewModel
            )
        }
    }

    private fun setLockerListViewModelHandler(
        item: AddLocker,
        viewModel: com.yapp.itemfinder.space.LockerListViewModel
    ) {
        item.addLockerHandler = { data ->
            viewModel.toAddLockerActivity()
        }
    }
}
