package com.yapp.itemfinder.feature.common.datalist

import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.model.ActionHandler
import com.yapp.itemfinder.feature.common.datalist.model.Data

abstract class DataListViewModel: BaseViewModel() {

    abstract fun fetchDataList(
        startHandler: ActionHandler? = null,
        successHandler: ActionHandler? = null,
        failureHandler: ActionHandler? = null,
        endHandler: ActionHandler? = null
    )

    abstract fun onResponseWith(dataList: List<Data>)

}
