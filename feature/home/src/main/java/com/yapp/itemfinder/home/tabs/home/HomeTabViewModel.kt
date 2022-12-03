package com.yapp.itemfinder.home.tabs.home

import com.yapp.itemfinder.feature.common.datalist.DataListViewModel
import com.yapp.itemfinder.feature.common.datalist.model.ActionHandler
import com.yapp.itemfinder.feature.common.datalist.model.Data
import kotlinx.coroutines.flow.MutableStateFlow

class HomeTabViewModel: DataListViewModel() {

    val homeTabStateFlow = MutableStateFlow<HomeTabState>(HomeTabState.Uninitialized)

    override fun fetchDataList(
        startHandler: ActionHandler?,
        successHandler: ActionHandler?,
        failureHandler: ActionHandler?,
        endHandler: ActionHandler?
    ) {
        startHandler?.invoke()
        successHandler?.invoke()
        onResponseWith(listOf())
        failureHandler?.invoke()
        endHandler?.invoke()
    }

    override fun onResponseWith(dataList: List<Data>) {
        homeTabStateFlow.value = HomeTabState.Success(dataList)
    }

}
