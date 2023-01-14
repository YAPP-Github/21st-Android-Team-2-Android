package com.yapp.itemfinder.space

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.SideEffect

sealed class LockerListSideEffect : SideEffect {
    data class MoveToLockerDetail(val locker: Locker) : LockerListSideEffect()
    object MoveToAddLocker : LockerListSideEffect()
}
