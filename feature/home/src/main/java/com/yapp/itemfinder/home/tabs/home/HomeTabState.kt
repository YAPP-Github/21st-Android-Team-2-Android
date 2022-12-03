package com.yapp.itemfinder.home.tabs.home

import com.yapp.itemfinder.domain.model.Data

sealed class HomeTabState {

    object Uninitialized: HomeTabState()

    object Loading: HomeTabState()

    data class Success(
        val dataList: List<Data>
    ): HomeTabState()

    data class Error(
        val e: Throwable
    ): HomeTabState()

}
