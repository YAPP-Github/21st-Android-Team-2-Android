package com.yapp.itemfinder.space.binder.additem.selectlocker

import com.yapp.itemfinder.domain.model.SelectLockerEntity
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.SelectLockerBinder
import com.yapp.itemfinder.space.additem.selectlocker.AddItemSelectLockerViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemSelectLockerBinder @Inject constructor() : SelectLockerBinder {

    override fun bindData(data: SelectLockerEntity, viewModel: BaseViewModel) {
        if (viewModel is AddItemSelectLockerViewModel) {
            setSelectLockerHandler(data, viewModel)
        }
    }

    private fun setSelectLockerHandler(data: SelectLockerEntity, viewModel: AddItemSelectLockerViewModel) {
        data.selectLockerHandler = {
            viewModel.selectLocker(it as SelectLockerEntity)
        }
    }

}
