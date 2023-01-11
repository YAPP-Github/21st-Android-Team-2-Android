package com.yapp.itemfinder.space.lockerdetail
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.State

sealed class LockerDetailState: State {
    data class Uninitialized(
        val locker: Locker
    ) : LockerDetailState()

    object Loading : LockerDetailState()

    data class Success(
        val locker: Locker,
        val dataList: List<Data>
    ) : LockerDetailState()

    data class Error(
        val e: Throwable
    ) : LockerDetailState()
}
