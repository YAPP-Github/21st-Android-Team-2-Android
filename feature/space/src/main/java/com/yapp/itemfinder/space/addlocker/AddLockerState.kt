package com.yapp.itemfinder.space.addlocker

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class AddLockerState : State {
    object Uninitialized : AddLockerState()

    object Loading : AddLockerState()

    data class Success(
        val dataList: List<Data>,
        val lockerName: String,
        val icon: String,
        val spaceId: Long,
        val url: String?,
        var isRefreshNeed: Boolean = true
    ) : AddLockerState()

    data class Error(
        val e: Throwable
    ) : AddLockerState()
}
