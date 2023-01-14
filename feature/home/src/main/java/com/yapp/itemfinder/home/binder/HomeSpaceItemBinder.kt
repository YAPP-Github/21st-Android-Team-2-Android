package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.SpaceItemBinder
import com.yapp.itemfinder.home.tabs.home.HomeTabViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeSpaceItemBinder @Inject constructor(): SpaceItemBinder{
    override fun bindData(data: SpaceItem, viewModel: BaseViewModel) {
        when (viewModel){
            is HomeTabViewModel -> setHomeTabViewModelHandler(data,viewModel)
        }
    }

    private fun setHomeTabViewModelHandler(item: SpaceItem, viewModel: HomeTabViewModel){
        item.detailHandler = { data ->
            viewModel.moveSpaceDetail(data as SpaceItem)
        }
    }
}
