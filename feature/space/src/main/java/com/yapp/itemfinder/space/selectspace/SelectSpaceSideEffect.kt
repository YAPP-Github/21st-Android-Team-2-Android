package com.yapp.itemfinder.space.selectspace

import com.yapp.itemfinder.feature.common.SideEffect

sealed class SelectSpaceSideEffect : SideEffect {
    data class ShowToast(val message: String) : SelectSpaceSideEffect()
}
