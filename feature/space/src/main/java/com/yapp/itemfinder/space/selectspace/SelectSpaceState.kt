package com.yapp.itemfinder.space.selectspace

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class SelectSpaceState : State {
    object Uninitialized : SelectSpaceState()
    data class Loading(
        val currentSpaceId: Long
    ) : SelectSpaceState()
    data class Success(
        val dataList: List<Data>
    ) : SelectSpaceState()

    data class Error(
        val e: Throwable
    ) : SelectSpaceState()
}
