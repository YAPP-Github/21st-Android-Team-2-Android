package com.yapp.itemfinder.space.managespace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageSpaceViewModel @Inject constructor(
    private val manageSpaceRepository: ManageSpaceRepository
) : BaseStateViewModel<ManageSpaceState, ManageSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<ManageSpaceState> =
        MutableStateFlow(ManageSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<ManageSpaceSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ManageSpaceState.Loading)
        var spaces = listOf<ManageSpaceEntity>()
        runCatchingWithErrorHandler {
            spaces = manageSpaceRepository.getAllManageSpaceItems()
        }.onSuccess {
            setState(
                ManageSpaceState.Success(
                    dataList = listOf(AddSpace()) + spaces
                )
            )
        }.onFailure { e ->
            setState(
                ManageSpaceState.Error(e)
            )
        }
    }

    fun openAddSpaceDialog(): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            postSideEffect(
                ManageSpaceSideEffect.OpenAddSpaceDialog
            )
        }
    }

    fun addItem(name: String): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            runCatchingWithErrorHandler {
                manageSpaceRepository.addNewSpace(name)
            }.onSuccess { space ->
                setState(
                    state.copy(
                        dataList = ArrayList(state.dataList).apply {
                            add(space)
                        }
                    )
                )
            }.onFailure { e ->
                setState(ManageSpaceState.Error(e))
                postSideEffect(
                    ManageSpaceSideEffect.AddSpaceFailedToast
                )
            }
        }
    }

    fun editItem(space: ManageSpaceEntity): Job = viewModelScope.launch {

    }

    fun deleteItem(space: ManageSpaceEntity): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            postSideEffect(
                ManageSpaceSideEffect.DeleteDialog
            )
        }
    }
}
