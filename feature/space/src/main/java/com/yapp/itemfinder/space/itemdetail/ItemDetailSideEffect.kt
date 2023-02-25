package com.yapp.itemfinder.space.itemdetail

import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class ItemDetailSideEffect : SideEffect {
    data class MoveToEditItem(
        val itemId: Long,
        val spaceAndLockerEntity: SpaceAndLockerEntity?,
    ) : ItemDetailSideEffect()

    object OpenDeleteDialog : ItemDetailSideEffect()

    data class ShowToast(val message: String) : ItemDetailSideEffect()

    object Finish : ItemDetailSideEffect()

}
