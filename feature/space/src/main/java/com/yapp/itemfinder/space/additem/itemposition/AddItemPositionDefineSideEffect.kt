package com.yapp.itemfinder.space.additem.itemposition

import com.yapp.itemfinder.domain.model.LockerAndItemEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemPositionDefineSideEffect : SideEffect {

    data class SaveItemPosition(
        val lockerAndItemEntity: LockerAndItemEntity
    ) : AddItemPositionDefineSideEffect()

}
