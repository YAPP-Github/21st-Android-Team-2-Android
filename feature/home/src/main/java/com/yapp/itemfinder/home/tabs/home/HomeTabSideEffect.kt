package com.yapp.itemfinder.home.tabs.home

import androidx.annotation.StringRes
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.SideEffect

sealed class HomeTabSideEffect : SideEffect {

    data class ShowToast(
        val message: String? = null,
        @StringRes val msgResId: Int? = null
    ) : HomeTabSideEffect()


    data class MoveSpaceDetail(val space: SpaceItem) : HomeTabSideEffect()
    data class MoveLockerDetail(val locker: LockerEntity) : HomeTabSideEffect()
    object MoveSpacesManage : HomeTabSideEffect()

    object ShowCreateNewSpacePopup : HomeTabSideEffect()
}
