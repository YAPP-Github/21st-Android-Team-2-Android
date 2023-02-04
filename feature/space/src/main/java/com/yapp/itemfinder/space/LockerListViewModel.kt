package com.yapp.itemfinder.space

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LockerListViewModel @Inject constructor(
    @LockerRepositoryQualifiers private val lockerRepository: LockerRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<LockerListState, LockerListSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerListState> =
        MutableStateFlow(LockerListState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LockerListSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(LockerListState.Loading)
        runCatchingWithErrorHandler {
            lockerRepository.getLockers(savedStateHandle.get<Long>(LockerListFragment.SPACE_ID_KEY)!!)
        }.onSuccess { lockers ->
            val spaceId = savedStateHandle.get<Long>(LockerListFragment.SPACE_ID_KEY)!!
            val spaceName = savedStateHandle.get<String>(LockerListFragment.SPACE_NAME_KEY)!!
            setState(
                LockerListState.Success(
                    dataList = listOf(AddLocker()) + lockers,
                    spaceId = spaceId,
                    spaceName = spaceName
                )
            )
        }.onErrorWithResult { errorWithResult ->
            setState(LockerListState.Error(errorWithResult))
            val message = errorWithResult.errorResultEntity.message
            message?.let { postSideEffect(LockerListSideEffect.ShowToast(message)) }
        }
    }

    fun moveAddLockerActivity(): Job = viewModelScope.launch {
        withState<LockerListState.Success> { state ->
            postSideEffect(LockerListSideEffect.MoveToAddLocker)
        }
    }

    fun moveAddItemActivity() = viewModelScope.launch {
        postSideEffect(LockerListSideEffect.MoveToAddItem)
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
