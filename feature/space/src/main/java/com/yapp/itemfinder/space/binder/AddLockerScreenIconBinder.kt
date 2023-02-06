package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.LockerIcons
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerIconBinder
import com.yapp.itemfinder.space.addlocker.AddLockerViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddLockerScreenIconBinder @Inject constructor() : AddLockerIconBinder {
    override fun bindData(data: LockerIcons, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddLockerViewModel -> {
                data.changeIconHandler = {
                    viewModel.setLockerIcon(it)
                }
            }
        }
    }
}
