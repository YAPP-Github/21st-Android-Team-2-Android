package com.yapp.itemfinder.space.managespace

import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class ManageSpaceSideEffect : SideEffect {

    data class OpenDeleteSpaceDialog(
        val spaceId: Long,
        val spaceName: String
    ) : ManageSpaceSideEffect()

    object OpenAddSpaceDialog : ManageSpaceSideEffect()

    data class OpenEditSpaceDialog(
        val space: ManageSpaceEntity
    ) : ManageSpaceSideEffect()

    data class ShowToast(
        val message: String
    ) : ManageSpaceSideEffect()

    object AddSpaceSuccessResult : ManageSpaceSideEffect()

}
