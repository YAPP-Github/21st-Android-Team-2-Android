package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.home.lockerlist.LockerListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLockerItemBinder @Inject constructor(): com.yapp.itemfinder.feature.common.datalist.binder.LockerItemBinder{
    override fun bindData(data: Locker, viewModel: BaseViewModel) {
        when (viewModel){
            is LockerListViewModel -> {}
        }
    }

}