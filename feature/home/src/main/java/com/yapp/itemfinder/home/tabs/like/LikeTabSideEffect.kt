package com.yapp.itemfinder.home.tabs.like

import com.yapp.itemfinder.feature.common.SideEffect

sealed class LikeTabSideEffect: SideEffect {

    data class ShowToast(val message: String): LikeTabSideEffect()

}
