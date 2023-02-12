package com.yapp.itemfinder.space.additem.selectlocker

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.State

sealed class AddItemSelectLockerState : State {

    object Uninitialized : AddItemSelectLockerState()

    object Loading : AddItemSelectLockerState()

    data class Success(
        val dataList: List<Data>,
    ) : AddItemSelectLockerState()

    data class Error(
        val e: Throwable
    ) : AddItemSelectLockerState()
}
