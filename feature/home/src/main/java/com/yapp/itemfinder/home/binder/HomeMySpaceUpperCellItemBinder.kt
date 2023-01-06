package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.CellItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.CellItemBinder
import com.yapp.itemfinder.home.tabs.home.HomeTabViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMySpaceUpperCellItemBinder @Inject constructor(): CellItemBinder{
    override fun bindData(data: CellItem, viewModel: BaseViewModel) {
        when (viewModel){
            is HomeTabViewModel -> setHomeTabViewModelHandler(data,viewModel)
        }
    }

    private fun setHomeTabViewModelHandler(item: CellItem, viewModel: HomeTabViewModel){
        item.action = {
            viewModel.goSpaceEdit()
        }
    }

}
