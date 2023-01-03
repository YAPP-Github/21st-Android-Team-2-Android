package com.yapp.itemfinder.home.lockerlist

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LockerListViewModel @Inject constructor(
    private val lockerMockRepository: LockerRepository
) : BaseStateViewModel<LockerListState, LockerListSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerListState> = MutableStateFlow(LockerListState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch{
        setState(LockerListState.Loading)

        val lockers = withContext(Dispatchers.IO) { lockerMockRepository.getAllLockers() }
        // val lockers = async { lockerMockRepository.getAllLockers() }.await()
        setState(
            LockerListState.Success(
                dataList = lockers
            )
        )

    }
}