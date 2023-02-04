package com.yapp.itemfinder.space.additem.selectspace

import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemSelectSpaceSideEffect : SideEffect {

    data class MoveAddItemSelectLocker(
        val spaceAndLockerEntity: SpaceAndLockerEntity
    ): AddItemSelectSpaceSideEffect()

}
