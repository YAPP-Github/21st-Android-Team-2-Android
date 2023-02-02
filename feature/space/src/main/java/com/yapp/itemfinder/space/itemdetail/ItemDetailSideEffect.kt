package com.yapp.itemfinder.space.itemdetail

import com.yapp.itemfinder.feature.common.SideEffect

sealed class ItemDetailSideEffect : SideEffect {
    object MoveToEditItem : ItemDetailSideEffect()
}
