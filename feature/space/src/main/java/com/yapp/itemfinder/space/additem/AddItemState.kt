package com.yapp.itemfinder.space.additem

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LockerAndItemEntity
import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.State

sealed class AddItemState : State {
    object Uninitialized: AddItemState()

    object Loading: AddItemState()

    data class Success(
        val dataList: List<Data>,
        val isRefreshNeed: Boolean = true,
        val spaceAndLockerEntity: SpaceAndLockerEntity? = null,
        val lockerAndItemEntity: LockerAndItemEntity? = null
    ): AddItemState()

    data class Error(
        val e: Throwable
    ): AddItemState()
}
