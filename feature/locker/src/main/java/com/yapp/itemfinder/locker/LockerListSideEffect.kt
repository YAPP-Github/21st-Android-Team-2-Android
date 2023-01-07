package com.yapp.itemfinder.locker

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.SideEffect

sealed class LockerListSideEffect: SideEffect {
    data class MoveToLockerDetail(val locker: Locker): LockerListSideEffect()
}
