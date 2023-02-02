package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.SelectSpaceBinder
import com.yapp.itemfinder.space.selectspace.SelectSpaceViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectSpaceBinderImpl @Inject constructor() : SelectSpaceBinder {
    override fun bindData(data: SelectSpace, viewModel: BaseViewModel) {
        when (viewModel) {
            is SelectSpaceViewModel -> setSelectSpaceViewModelHandler(data, viewModel)
        }
    }

    private fun setSelectSpaceViewModelHandler(
        selectSpace: SelectSpace,
        viewModel: SelectSpaceViewModel
    ) {
        selectSpace.checkHandler = {
            viewModel.changeChecked(selectSpace)
        }
    }

}
