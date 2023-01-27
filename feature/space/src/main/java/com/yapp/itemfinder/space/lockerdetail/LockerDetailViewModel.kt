package com.yapp.itemfinder.space.lockerdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockerDetailViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    @LockerRepositoryQualifiers private val lockerRepository: LockerRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {

    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized)

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()

    private val locker by lazy { savedStateHandle.get<LockerEntity>(LockerDetailFragment.LOCKER_ENTITY_KEY) }

    override fun fetchData(): Job = viewModelScope.launch {
        // api를 붙일 경우, args의 id를 활용하세요
        runCatchingWithErrorHandler {
            setState(LockerDetailState.Loading)
            locker?.let {
                it to itemRepository.getItemsByLockerId(it.id)
            } ?: throw IllegalArgumentException("보관함 정보를 불러올 수 없습니다.")
        }.onSuccess { (locker, items) ->
            setState(
                LockerDetailState.Success(
                    locker = locker,
                    dataList = items
                )
            )

        }.onFailure {
            setState(LockerDetailState.Error(it))
        }
    }

    fun moveItemDetail(itemId: Long) {
        postSideEffect(LockerDetailSideEffect.MoveItemDetail(itemId))
    }
}
