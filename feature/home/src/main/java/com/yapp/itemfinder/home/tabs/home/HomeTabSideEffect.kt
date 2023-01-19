package com.yapp.itemfinder.home.tabs.home

import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.MySpaceUpperCellItem
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.SideEffect

sealed class HomeTabSideEffect : SideEffect {

    data class ShowToast(
        val message: String
    ) : HomeTabSideEffect()

    data class MoveSpaceDetail(
        val space: SpaceItem
    ) : HomeTabSideEffect()

    data class MoveLockerDetail(
        val locker: LockerEntity
    ) : HomeTabSideEffect()

    data class MoveSpacesManage(
        val mySpaceUpperCellItem: MySpaceUpperCellItem
    ) : HomeTabSideEffect()

    object ShowCreateNewSpacePopup : HomeTabSideEffect()

}
