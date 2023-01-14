package com.yapp.itemfinder.space.addLocker

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class AddLockerState : State {
    object Uninitialized: AddLockerState()

    object Loading: AddLockerState()

    data class Success(
        val dataList: List<Data>
    ): AddLockerState()

    data class Error(
        val e: Throwable
    ): AddLockerState()
}
