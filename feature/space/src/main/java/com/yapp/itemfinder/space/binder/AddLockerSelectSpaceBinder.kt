package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddLockerSpace
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerSpaceBinder
import com.yapp.itemfinder.space.addLocker.AddLockerViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddLockerSelectSpaceBinder @Inject constructor(): AddLockerSpaceBinder {
    override fun bindData(data: AddLockerSpace, viewModel: BaseViewModel) {
        when (viewModel){
            is AddLockerViewModel -> setAddLockerViewModelHandler(
                data, viewModel
            )
        }
    }
    
    private fun setAddLockerViewModelHandler(
        item: AddLockerSpace,
        viewModel: AddLockerViewModel
    ){
        item.selectSpaceHandler = {
            viewModel.openSelectSpace()
        }
    }
}
