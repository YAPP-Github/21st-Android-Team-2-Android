package com.yapp.itemfinder.space.additem.itemposition

import com.yapp.itemfinder.domain.model.LockerAndItemEntity
import com.yapp.itemfinder.feature.common.State

sealed class AddItemPositionDefineState : State {

    object Uninitialized : AddItemPositionDefineState()

    object Loading : AddItemPositionDefineState()

    data class Success(
        val lockerAndItemEntity: LockerAndItemEntity
    ) : AddItemPositionDefineState()

    data class Error(
        val e: Throwable
    ) : AddItemPositionDefineState()

}
