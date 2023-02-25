package com.yapp.itemfinder.space.lockerdetail

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.feature.common.State
import com.yapp.itemfinder.space.lockerdetail.itemfilter.ItemFilterCondition

sealed class LockerDetailState : State {
    object Uninitialized : LockerDetailState()

    object Loading : LockerDetailState()

    data class Success(
        val itemFilterCondition: ItemFilterCondition = ItemFilterCondition.NONE,
        val locker: LockerEntity,
        val space: ManageSpaceEntity? = null,
        val dataList: List<Data>,
        val needToFetch: Boolean = true,
        val lastFocusedItem: Item? = null,
        val focusIndex: Int? = null,
    ) : LockerDetailState()

    data class Error(
        val e: Throwable
    ) : LockerDetailState()
}
