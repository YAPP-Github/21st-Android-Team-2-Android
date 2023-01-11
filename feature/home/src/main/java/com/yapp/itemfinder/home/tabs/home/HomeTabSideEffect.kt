package com.yapp.itemfinder.home.tabs.home

import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.SideEffect

sealed class HomeTabSideEffect: SideEffect {

    data class ShowToast(val message: String): HomeTabSideEffect()
    data class MoveSpaceDetail(val space: SpaceItem): HomeTabSideEffect()
    data class MoveLockerDetail(val locker: Locker): HomeTabSideEffect()
    object MoveSpacesManage: HomeTabSideEffect()
}
