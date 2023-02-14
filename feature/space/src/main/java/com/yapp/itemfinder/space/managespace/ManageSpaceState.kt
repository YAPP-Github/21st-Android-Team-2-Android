package com.yapp.itemfinder.space.managespace

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class ManageSpaceState : State {
    object Uninitialized : ManageSpaceState()

    object Loading : ManageSpaceState()

    data class Success(
        val dataList: List<Data>
    ) : ManageSpaceState()

    object Error : ManageSpaceState()
}
