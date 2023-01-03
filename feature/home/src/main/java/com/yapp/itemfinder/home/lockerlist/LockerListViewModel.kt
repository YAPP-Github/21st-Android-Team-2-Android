package com.yapp.itemfinder.home.lockerlist

import com.yapp.itemfinder.feature.common.BaseStateViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class LockerListViewModel() : BaseStateViewModel<LockerListState, LockerListSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerListState> = MutableStateFlow(LockerListState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job {
        return super.fetchData()
    }
}