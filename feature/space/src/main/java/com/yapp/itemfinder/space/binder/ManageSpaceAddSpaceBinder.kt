package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddSpaceBinder
import com.yapp.itemfinder.space.managespace.ManageSpaceViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageSpaceAddSpaceBinder @Inject constructor(): AddSpaceBinder{
    override fun bindData(data: AddSpace, viewModel: BaseViewModel) {
        when (viewModel) {
            is ManageSpaceViewModel -> setManageSpaceViewModelHandler(data, viewModel)
        }
    }

    private fun setManageSpaceViewModelHandler(data: AddSpace, viewModel: ManageSpaceViewModel){
        data.addSpaceHandler = {
            viewModel.addItem()
        }
    }
}
