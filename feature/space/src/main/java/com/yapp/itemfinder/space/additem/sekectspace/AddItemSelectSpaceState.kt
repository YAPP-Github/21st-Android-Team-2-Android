package com.yapp.itemfinder.space.additem.sekectspace

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class AddItemSelectSpaceState : State {

    object Uninitialized : AddItemSelectSpaceState()

    object Loading : AddItemSelectSpaceState()

    data class Success(
        val dataList: List<Data>,
    ) : AddItemSelectSpaceState()

    data class Error(
        val e: Throwable
    ) : AddItemSelectSpaceState()
}
