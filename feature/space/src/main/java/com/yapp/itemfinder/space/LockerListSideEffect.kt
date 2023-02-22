package com.yapp.itemfinder.space

import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class LockerListSideEffect : SideEffect {
    data class MoveToLockerDetail(val locker: LockerEntity) : LockerListSideEffect()
    object MoveToAddLocker : LockerListSideEffect()
    data class MoveToEditLocker(val locker: LockerEntity) : LockerListSideEffect()
    object MoveToAddItem : LockerListSideEffect()
    data class OpenDeleteLockerDialog(val lockerName: String, val lockerId: Long) :
        LockerListSideEffect()

    data class ShowToast(val message: String) : LockerListSideEffect()
}
