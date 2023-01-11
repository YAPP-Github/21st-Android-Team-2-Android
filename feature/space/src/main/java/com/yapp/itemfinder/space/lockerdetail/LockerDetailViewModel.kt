package com.yapp.itemfinder.space.lockerdetail

import com.yapp.itemfinder.feature.common.BaseStateViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LockerDetailViewModel() : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized)

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()
}
