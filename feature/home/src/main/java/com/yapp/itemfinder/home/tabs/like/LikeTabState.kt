package com.yapp.itemfinder.home.tabs.like

import com.yapp.itemfinder.domain.model.Data

sealed class LikeTabState {

    object Uninitialized : LikeTabState()

    object Loading : LikeTabState()

    data class Success(
        val dataList: List<Data>
    ) : LikeTabState()

    data class Error(
        val e: Throwable
    ) : LikeTabState()
}