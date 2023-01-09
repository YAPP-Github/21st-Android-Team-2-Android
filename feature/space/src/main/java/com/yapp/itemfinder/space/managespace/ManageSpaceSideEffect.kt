package com.yapp.itemfinder.space.managespace

import com.yapp.itemfinder.feature.common.SideEffect

sealed class ManageSpaceSideEffect: SideEffect {
    object DeleteDialog: ManageSpaceSideEffect()
}
