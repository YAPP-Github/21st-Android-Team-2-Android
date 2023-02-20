package com.yapp.itemfinder.space.lockerdetail

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.State

sealed class LockerDetailState : State {
    object Uninitialized : LockerDetailState()

    object Loading : LockerDetailState()

    data class Success(
        val locker: LockerEntity,
        val dataList: List<Data>,
        val needToFetch: Boolean = true,
        val lastFocusedItem: Item? = null,
        val focusIndex: Int? = null,
    ) : LockerDetailState()

    data class Error(
        val e: Throwable
    ) : LockerDetailState()
}
