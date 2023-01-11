package com.yapp.itemfinder.space.lockerdetail

import android.os.Bundle
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LockerDetailViewModel(args: Bundle) : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized(locker = args.get("locker") as Locker))

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()
}
