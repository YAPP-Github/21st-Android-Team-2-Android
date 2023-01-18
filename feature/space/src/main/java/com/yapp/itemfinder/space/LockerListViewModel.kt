package com.yapp.itemfinder.space

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LockerListViewModel @Inject constructor(
    @LockerRepositoryQualifiers private val lockerRepository: LockerRepository
) : BaseStateViewModel<LockerListState, LockerListSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerListState> =
        MutableStateFlow(LockerListState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect> = MutableSharedFlow()

    // fetchData를 이용해서 읽어오는 작업으로 변갱해주세요!
    override fun fetchData(): Job = viewModelScope.launch {

    }

    fun fetchLockerList(spaceId: Long) = viewModelScope.launch {
        setState(LockerListState.Loading)
        runCatchingWithErrorHandler {
            lockerRepository.getLockers(spaceId)
        }.onSuccess { lockers ->
            setState(
                LockerListState.Success(
                    dataList = listOf(AddLocker()) + lockers
                )
            )
        }.onFailure {

        }
    }

    fun moveAddLockerActivity(): Job = viewModelScope.launch {
        withState<LockerListState.Success> { state ->
            postSideEffect(LockerListSideEffect.MoveToAddLocker)
        }
    }

    fun editItem(item: LockerEntity): Job = viewModelScope.launch {

    }

    fun deleteItem(item: LockerEntity): Job = viewModelScope.launch {
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

    fun moveLockerDetail(locker: LockerEntity) {
        postSideEffect(LockerListSideEffect.MoveToLockerDetail(locker))
    }
}
