package com.yapp.itemfinder.space

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class LockerListState : State {
    object Uninitialized : LockerListState()

    object Loading : LockerListState()

    data class Success(
        val dataList: List<Data>,
        val spaceId: Long,
        val spaceName: String
    ) : LockerListState()

    data class Error(
        val e: Throwable
    ) : LockerListState()
}
