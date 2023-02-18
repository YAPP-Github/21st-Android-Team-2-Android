package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.ItemBinder
import com.yapp.itemfinder.space.lockerdetail.LockerDetailViewModel
import javax.inject.Inject

class SimpleItemBinder @Inject constructor() : ItemBinder {
    override fun bindData(data: Item, viewModel: BaseViewModel) {
        when(viewModel){
            is LockerDetailViewModel -> {
                setLockerDetailViewModelHandlers(data, viewModel)
            }
        }

    }
    private fun setLockerDetailViewModelHandlers(data: Item, vm: LockerDetailViewModel){
        data.itemDetailHandler = {
            vm.moveItemDetail(data.id)
        }
    }
}
