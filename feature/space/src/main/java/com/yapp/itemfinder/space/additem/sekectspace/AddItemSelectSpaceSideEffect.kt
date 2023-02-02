package com.yapp.itemfinder.space.additem.sekectspace

import com.yapp.itemfinder.domain.model.AddItemSelectSpaceEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemSelectSpaceSideEffect : SideEffect {

    data class MoveAddItemSelectLocker(
        val addItemSelectSpaceEntity: AddItemSelectSpaceEntity
    ): AddItemSelectSpaceSideEffect()

}
