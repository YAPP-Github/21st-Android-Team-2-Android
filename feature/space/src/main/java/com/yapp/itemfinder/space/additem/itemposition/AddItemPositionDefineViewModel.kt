package com.yapp.itemfinder.space.additem.itemposition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerAndItemEntity
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.space.additem.AddItemActivity.Companion.LOCKER_AND_ITEM_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemPositionDefineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) :
    BaseStateViewModel<AddItemPositionDefineState, AddItemPositionDefineSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddItemPositionDefineState> =
        MutableStateFlow(AddItemPositionDefineState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddItemPositionDefineSideEffect> = MutableSharedFlow()

    private val lockerAndItemEntity by lazy { savedStateHandle.get<LockerAndItemEntity>(LOCKER_AND_ITEM_KEY)!! }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            AddItemPositionDefineState.Success(lockerAndItemEntity)
        )
    }

    fun initializePin() {
        withState<AddItemPositionDefineState.Success> { state ->
            setState(
                state.copy(
                    lockerAndItemEntity = lockerAndItemEntity.copy(
                        item = lockerAndItemEntity.item?.copy(
                            position = null
                        )
                    )
                )
            )
        }
    }

    fun setItem(item: Item) {
        withState<AddItemPositionDefineState.Success> { state ->
            setState(
                state.copy(
                    lockerAndItemEntity = lockerAndItemEntity.copy(
                        item = state.lockerAndItemEntity.item?.copy(
                            position = item.position,
                            itemCategory = state.lockerAndItemEntity.item?.itemCategory
                        ) ?: item
                    )
                )
            )
        }
    }

    fun saveItemPosition() {
        withState<AddItemPositionDefineState.Success> { state ->
            postSideEffect(
                AddItemPositionDefineSideEffect.SaveItemPosition(
                    state.lockerAndItemEntity
                )
            )
        }
    }

}
