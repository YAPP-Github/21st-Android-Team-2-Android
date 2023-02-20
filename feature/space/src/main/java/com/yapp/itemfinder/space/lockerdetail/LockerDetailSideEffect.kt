package com.yapp.itemfinder.space.lockerdetail

import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class LockerDetailSideEffect : SideEffect {
    data class MoveItemDetail(
        val itemId: Long,
        val spaceAndLockerEntity: SpaceAndLockerEntity
    ) : LockerDetailSideEffect()
}
