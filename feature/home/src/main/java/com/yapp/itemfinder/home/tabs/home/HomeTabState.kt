package com.yapp.itemfinder.home.tabs.home

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class HomeTabState: State {

    object Uninitialized: HomeTabState()

    object Loading: HomeTabState()

    data class Success(
        val dataList: List<Data>
    ): HomeTabState()

    data class Error(
        val e: Throwable
    ): HomeTabState()

}
