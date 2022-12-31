package com.yapp.itemfinder.home.binder

import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.LikeItemBinder
import com.yapp.itemfinder.home.tabs.home.HomeTabViewModel
import com.yapp.itemfinder.home.tabs.like.LikeTabViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLikeItemBinder @Inject constructor(): LikeItemBinder {

    override fun bindData(data: LikeItem, viewModel: BaseViewModel) {
        when (viewModel) {
            is LikeTabViewModel -> setLikeTabViewModelHandler(data, viewModel)
            is HomeTabViewModel -> setHomeTabViewModelHandler(data, viewModel)
        }
    }

    private fun setLikeTabViewModelHandler(item: LikeItem, viewModel: LikeTabViewModel) {
        item.deleteHandler = { data ->
            viewModel.deleteItem(data as LikeItem)
        }
        item.updateHandler = { data ->
            viewModel.updateCount(data as LikeItem)
        }
    }

    private fun setHomeTabViewModelHandler(item: LikeItem, viewModel: HomeTabViewModel) {
        item.updateHandler = { data ->
            viewModel.updateCount(data as LikeItem)
        }
    }

}