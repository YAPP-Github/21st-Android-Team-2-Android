package com.yapp.itemfinder.space.lockerdetail
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.State

sealed class LockerDetailState: State {
    data class Uninitialized(
        val locker: LockerEntity
    ) : LockerDetailState()

    object Loading : LockerDetailState()

    data class Success(
        val locker: LockerEntity,
        val dataList: List<Data>
    ) : LockerDetailState()

    data class Error(
        val e: Throwable
    ) : LockerDetailState()
}
