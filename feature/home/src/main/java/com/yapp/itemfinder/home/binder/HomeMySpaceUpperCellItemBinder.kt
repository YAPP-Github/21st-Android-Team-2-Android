package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.MySpaceUpperCellItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.CellItemBinder
import com.yapp.itemfinder.home.tabs.home.HomeTabViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMySpaceUpperCellItemBinder @Inject constructor(): CellItemBinder{
    override fun bindData(data: MySpaceUpperCellItem, viewModel: BaseViewModel) {
        when (viewModel){
            is HomeTabViewModel -> setHomeTabViewModelHandler(data,viewModel)
        }
    }

    private fun setHomeTabViewModelHandler(item: MySpaceUpperCellItem, viewModel: HomeTabViewModel){
        item.runSpaceManagementPage = {
            viewModel.runSpaceManagementPage()
        }
    }

}
