package com.yapp.itemfinder.space.lockerdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.ItemMockRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.ItemRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.data.repositories.di.SpaceRepositoryQualifiers
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.domain.repository.SpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.lockerdetail.itemfilter.ItemFilterCondition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockerDetailViewModel @Inject constructor(
    @ItemMockRepositoryQualifiers
    private val itemMockRepository: ItemRepository,
    @ItemRepositoryQualifiers
    private val itemRepository: ItemRepository,
    @LockerRepositoryQualifiers
    private val lockerRepository: LockerRepository,
    @SpaceRepositoryQualifiers
    private val spaceRepository: SpaceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {

    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized)

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()

    private val locker by lazy { savedStateHandle.get<LockerEntity>(LockerDetailFragment.LOCKER_ENTITY_KEY) }
    private lateinit var space: ManageSpaceEntity

    override fun fetchData(): Job = viewModelScope.launch {
        // api를 붙일 경우, args의 id를 활용하세요
        runCatchingWithErrorHandler {
            setState(LockerDetailState.Loading)
            locker?.let {
                val lockerEntity = async { lockerRepository.getLockerById(it.id)}
                val items = async { itemRepository.getItemsByLockerId(it.id) }
                lockerEntity.await() to items.await()
            } ?: throw IllegalArgumentException("보관함 정보를 불러올 수 없습니다.")
        }.onSuccess { (locker, items) ->
            var space: ManageSpaceEntity = ManageSpaceEntity("")
            runCatchingWithErrorHandler {
                spaceRepository.getSpaceById(locker.spaceId)
            }.onSuccess { s ->
                space = s
            }
            setState(
                LockerDetailState.Success(
                    locker = locker,
                    dataList = items,
                    space = space
                )
            )

        }.onErrorWithResult { errorWithResult ->
            val message = errorWithResult.errorResultEntity.message
            // setState(LockerDetailState.Error(it))
        }
    }

    fun reFetchData(): Job = viewModelScope.launch {
        withState<LockerDetailState.Success> { prevState ->
            runCatchingWithErrorHandler {
                setState(LockerDetailState.Loading)
                locker?.let { locker ->
                    lockerRepository.getLockers(locker.spaceId).find { it.id == locker.id }?.let { foundLocker ->
                        foundLocker to itemRepository.getItemsByLockerId(foundLocker.id)
                    } ?: throw IllegalArgumentException("보관함 정보를 불러올 수 없습니다.")
                } ?: throw IllegalArgumentException("보관함 정보를 불러올 수 없습니다.")
            }.onSuccess { (locker, items) ->
                setState(
                    LockerDetailState.Success(
                        locker = locker,
                        dataList = items
                    )
                )
                withState<LockerDetailState.Success> { state ->
                    val item =
                        state.dataList.find { it.id == prevState.lastFocusedItem?.id } as Item
                    setState(
                        state.copy(
                            itemFilterCondition = prevState.itemFilterCondition,
                            needToFetch = false,
                        )
                    )
//                    val item = state.dataList.find { it.id == prevState.lastFocusedItem?.id } as Item
//                    setState(
//                        state.copy(
//                            itemFilterCondition = prevState.itemFilterCondition,
//                            needToFetch = false,
//                            lastFocusedItem = item,
//                            focusIndex = prevState.focusIndex
//                        )
//                    )
//
//                    item.applyItemFocus(true)
                }
            }.onErrorWithResult { errorWithResult ->
                val message = errorWithResult.errorResultEntity.message
                // setState(LockerDetailState.Error(it))
            }
        }
    }

    fun moveItemDetail(itemId: Long) = viewModelScope.launch {
        withState<LockerDetailState.Success> { state ->
            runCatchingWithErrorHandler {
                val spaces = spaceRepository.getAllSpaces()
                val space = spaces.find { it.id == state.locker.spaceId }
                space ?: throw IllegalArgumentException("공간을 찾을 수 없습니다.")
                space to locker
            }.onSuccess { (space, locker) ->
                postSideEffect(
                    LockerDetailSideEffect.MoveItemDetail(
                        itemId,
                        SpaceAndLockerEntity(space.toManageSpaceEntity(), locker)
                    )
                )
            }.onFailure {
                setState(LockerDetailState.Error(it))
            }
        }
    }

    fun applyFocusFirstItem(position: Int) {
        withState<LockerDetailState.Success> { state ->
            state.lastFocusedItem?.applyItemFocus(false)
            if (state.dataList.isNotEmpty()) {
                val focusItem = state.dataList[position] as Item
                focusItem.applyItemFocus(true)
                setState(
                    state.copy(
                        needToFetch = false,
                        lastFocusedItem = focusItem,
                        focusIndex = position
                    )
                )
            }
        }
    }

    fun moveToItemFilter() {
        withState<LockerDetailState.Success> { state ->
            postSideEffect(
                LockerDetailSideEffect.MoveItemFilter(
                    state.itemFilterCondition
                )
            )
        }
    }

    fun setItemFilterCondition(itemFilterCondition: ItemFilterCondition?) {
        withState<LockerDetailState.Success> { state ->
            setState(
                state.copy(
                    itemFilterCondition = itemFilterCondition ?: state.itemFilterCondition
                )
            )
        }
    }

    fun getLockerInfo(): LockerEntity? {
        var locker: LockerEntity? = null
        withState<LockerDetailState.Success> { state ->
            locker = state.locker
        }
        return locker
    }

    fun getSpaceInfo(): ManageSpaceEntity? {
        var space: ManageSpaceEntity? = null
        withState<LockerDetailState.Success> { state ->
            space = state.space
        }
        return space
    }

}
