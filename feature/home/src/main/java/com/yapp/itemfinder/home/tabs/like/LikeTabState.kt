package com.yapp.itemfinder.home.tabs.like

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class LikeTabState: State {

    object Uninitialized : LikeTabState()

    object Loading : LikeTabState()

    data class Success(
        val dataList: List<Data>
    ) : LikeTabState()

    data class Error(
        val e: Throwable
    ) : LikeTabState()
}
