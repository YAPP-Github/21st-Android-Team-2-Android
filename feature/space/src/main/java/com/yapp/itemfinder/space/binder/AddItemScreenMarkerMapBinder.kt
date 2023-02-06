package com.yapp.itemfinder.space.binder

import com.yapp.itemfinder.domain.model.AddItemMarkerMap
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemMarkerMapBinder
import com.yapp.itemfinder.space.additem.AddItemViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddItemScreenMarkerMapBinder @Inject constructor() : AddItemMarkerMapBinder {

    override fun bindData(data: AddItemMarkerMap, viewModel: BaseViewModel) {
        when (viewModel) {
            is AddItemViewModel -> setAddItemMarkerMapHandler(data, viewModel)
        }
    }

    private fun setAddItemMarkerMapHandler(data: AddItemMarkerMap, viewModel: AddItemViewModel) {
        data.moveItemPositionDefineHandler = {
            viewModel.moveItemPositionDefine()
        }
    }

}
