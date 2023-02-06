package com.yapp.itemfinder.space.managespace

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.SideEffect

sealed class ManageSpaceSideEffect : SideEffect {
    object DeleteDialog : ManageSpaceSideEffect()
    object OpenAddSpaceDialog : ManageSpaceSideEffect()
    object AddSpaceFailedToast : ManageSpaceSideEffect()

    object AddSpaceSuccessResult : ManageSpaceSideEffect()
}
