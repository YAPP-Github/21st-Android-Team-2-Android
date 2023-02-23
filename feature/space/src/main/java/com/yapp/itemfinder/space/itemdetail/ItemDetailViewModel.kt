package com.yapp.itemfinder.space.itemdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.ItemRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.SpaceRepositoryQualifiers
import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.SpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    @ItemRepositoryQualifiers private val itemRepository: ItemRepository,
    @SpaceRepositoryQualifiers private val spaceRepository: SpaceRepository,
    @LockerRepositoryQualifiers private val lockerRepository: LockerRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<ItemDetailState, ItemDetailSideEffect>() {

    override val _stateFlow: MutableStateFlow<ItemDetailState> =
        MutableStateFlow(ItemDetailState.Uninitialized)

    override val _sideEffectFlow: MutableSharedFlow<ItemDetailSideEffect> = MutableSharedFlow()

    private val itemId by lazy {
        savedStateHandle.get<Long>(ItemDetailFragment.ITEM_ID_KEY) ?: 0L
    }

    private val spaceAndLockerEntity by lazy {
        savedStateHandle.get<SpaceAndLockerEntity>(ItemDetailFragment.SPACE_AND_LOCKER_KEY)
    }

    override fun fetchData(): Job = viewModelScope.launch {
        runCatchingWithErrorHandler {
            setState(ItemDetailState.Loading)
            val item = itemId.let { itemRepository.getItemById(itemId) }
            item
        }.onSuccess { item ->
            setState(
                ItemDetailState.Success(
                    item = item,
                    spaceAndLockerEntity = spaceAndLockerEntity
                )
            )
        }.onErrorWithResult { errorWithResult ->
            setState(ItemDetailState.Error)
            val message = errorWithResult.errorResultEntity.message ?: return@launch
            postSideEffect(
                message.let { ItemDetailSideEffect.ShowToast(it) }
            )
        }
    }

    fun reFetchData(): Job = viewModelScope.launch {
        withState<ItemDetailState.Success> { state ->
            runCatchingWithErrorHandler {
                setState(ItemDetailState.Loading)
                val item = itemId.let { itemRepository.getItemById(itemId) }
                val lockerId = item.lockerId ?: state.spaceAndLockerEntity?.lockerEntity?.id
                ?: throw IllegalArgumentException("보관함을 찾을 수 없습니다.")
                val locker = lockerRepository.getLockerById(lockerId)
                val spaces = spaceRepository.getAllSpaces()
                val space = spaces.find { it.id == locker?.spaceId }
                    ?: throw IllegalArgumentException("공간을 찾을 수 없습니다.")
                val spaceAndLockerEntity = SpaceAndLockerEntity(space.toManageSpaceEntity(), locker)
                item to spaceAndLockerEntity
            }.onSuccess { (item, spaceAndLockerEntity) ->
                setState(
                    ItemDetailState.Success(
                        item = item,
                        spaceAndLockerEntity = spaceAndLockerEntity
                    )
                )
            }.onErrorWithResult { errorWithResult ->
                setState(ItemDetailState.Error)
                val message = errorWithResult.errorResultEntity.message ?: return@launch
                postSideEffect(
                    message.let { ItemDetailSideEffect.ShowToast(it) }
                )
            }
        }
    }

    fun moveToEdit() = viewModelScope.launch {
        withState<ItemDetailState.Success> { state ->
            postSideEffect(
                ItemDetailSideEffect.MoveToEditItem(
                    itemId = itemId,
                    spaceAndLockerEntity = state.spaceAndLockerEntity
                )
            )
        }
    }

    fun openDeleteDialog() = viewModelScope.launch {
        postSideEffect(ItemDetailSideEffect.OpenDeleteDialog)
    }

    fun deleteItem() = viewModelScope.launch {
        withState<ItemDetailState.Success> { state ->
            runCatchingWithErrorHandler {
                itemRepository.deleteItem(state.item.id)
            }.onSuccess {
                postSideEffect(
                    ItemDetailSideEffect.Finish
                )
            }.onErrorWithResult { errorWithResult ->
                setState(ItemDetailState.Error)
                val message = errorWithResult.errorResultEntity.message ?: return@launch
                postSideEffect(
                    message.let { ItemDetailSideEffect.ShowToast(it) }
                )
            }
        }
    }
}
