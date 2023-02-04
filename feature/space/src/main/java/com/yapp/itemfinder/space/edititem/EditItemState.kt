package com.yapp.itemfinder.space.edititem

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class EditItemState : State {
    object Uninitialized: EditItemState()

    object Loading: EditItemState()

    data class Success(
        val dataList: List<Data>,
        val isRefreshNeed: Boolean = true
    ): EditItemState()

    data class Error(
        val e: Throwable
    ): EditItemState()
}
