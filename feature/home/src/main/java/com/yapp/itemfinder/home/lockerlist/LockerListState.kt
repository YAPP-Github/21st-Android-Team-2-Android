package com.yapp.itemfinder.home.lockerlist

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State
import com.yapp.itemfinder.home.tabs.like.LikeTabState

sealed class LockerListState: State {
    object Uninitialized : LockerListState()

    object Loading : LockerListState()

    data class Success(
        val dataList: List<Data>
    ) : LockerListState()

    data class Error(
        val e: Throwable
    ) : LockerListState()
}