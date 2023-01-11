package com.yapp.itemfinder.space.lockerdetail

import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.space.LockerListSideEffect
import com.yapp.itemfinder.space.LockerListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LockerDetailViewModel(
    override val _stateFlow: MutableStateFlow<LockerListState>,
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect>
) : BaseStateViewModel<LockerListState, LockerListSideEffect>() {
}
