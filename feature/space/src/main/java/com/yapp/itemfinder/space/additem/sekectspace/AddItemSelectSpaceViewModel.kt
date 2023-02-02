package com.yapp.itemfinder.space.additem.sekectspace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddItemSelectSpaceEntity
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemSelectSpaceViewModel @Inject constructor(
    private val manageSpaceRepository: ManageSpaceRepository,
    private val savedStateHandle: SavedStateHandle
) :
    BaseStateViewModel<AddItemSelectSpaceState, AddItemSelectSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddItemSelectSpaceState> =
        MutableStateFlow(AddItemSelectSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddItemSelectSpaceSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        val addItemSelectSpaceEntities =
            manageSpaceRepository.getAllManageSpaceItems().map { it.mapAddItemSelectSpaceEntity() }

        setState(
            AddItemSelectSpaceState.Success(
                dataList = addItemSelectSpaceEntities,
            )
        )
    }

    fun selectSpace(addItemSelectSpaceEntity: AddItemSelectSpaceEntity) {
        postSideEffect(
            AddItemSelectSpaceSideEffect.MoveAddItemSelectLocker(addItemSelectSpaceEntity)
        )
    }

}
