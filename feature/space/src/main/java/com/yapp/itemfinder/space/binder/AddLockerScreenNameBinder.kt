package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddLockerNameInput
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerNameBinder
import com.yapp.itemfinder.space.addlocker.AddLockerViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddLockerScreenNameBinder @Inject constructor() : AddLockerNameBinder {
    override fun bindData(data: AddLockerNameInput, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddLockerViewModel -> {
                data.enterHandler = {
                    viewModel.setLockerName(it)
                }
            }
        }
    }
}
