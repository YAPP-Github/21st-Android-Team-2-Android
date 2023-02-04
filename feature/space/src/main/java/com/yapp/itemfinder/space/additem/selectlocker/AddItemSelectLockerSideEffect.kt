package com.yapp.itemfinder.space.additem.selectlocker

import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemSelectLockerSideEffect : SideEffect {

    data class SavePath(
        val spaceAndLockerEntity: SpaceAndLockerEntity
    ): AddItemSelectLockerSideEffect()

    data class ShowToast(val message: String): AddItemSelectLockerSideEffect()

}
