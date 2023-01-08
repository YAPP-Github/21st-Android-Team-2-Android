package com.yapp.itemfinder.space

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.Locker
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
    override val _stateFlow: MutableStateFlow<LockerListState> =
        MutableStateFlow(LockerListState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect> = MutableSharedFlow()

    var tmpId = 4L
    override fun fetchData(): Job = viewModelScope.launch {
        setState(LockerListState.Loading)

        val lockers = withContext(Dispatchers.IO) { lockerMockRepository.getAllLockers() }
        setState(
            LockerListState.Success(
                dataList = lockers
            )
        )
    }

    fun addItem(): Job = viewModelScope.launch {
        withState<LockerListState.Success> { state ->
            setState(
                state.copy(
                    ArrayList(state.dataList).apply {
                        add(
                            Locker(id = tmpId++, name = "새로운 locker", com.yapp.itemfinder.domain.R.drawable.box)
                        )
                    }
                )
            )
        }
    }

    fun editItem(item: Locker): Job = viewModelScope.launch {

    }

    fun deleteItem(item: Locker): Job = viewModelScope.launch {
        withState<LockerListState.Success> { state ->
            setState(
                state.copy(
                    state.dataList.toMutableList().apply {
                        remove(item)
                    }
                )
            )
        }
    }
}
