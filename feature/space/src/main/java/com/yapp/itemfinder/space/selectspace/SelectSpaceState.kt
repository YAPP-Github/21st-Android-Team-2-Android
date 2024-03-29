package com.yapp.itemfinder.space.selectspace

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class SelectSpaceState : State {
    object Uninitialized : SelectSpaceState()
    object Loading : SelectSpaceState()
    data class Success(
        val dataList: List<Data>,
        var spaceId: Long,
        var spaceName: String,
        var checkedIndex: Int
    ) : SelectSpaceState()

    data class Error(
        val e: Throwable
    ) : SelectSpaceState()
}
