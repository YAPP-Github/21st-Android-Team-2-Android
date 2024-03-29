package com.yapp.itemfinder.space.managespace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
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
        }.onErrorWithResult { errorWithResult ->
            setState(ManageSpaceState.Error)
            val message = errorWithResult.errorResultEntity.message ?: return@launch
            postSideEffect(
                message.let { ManageSpaceSideEffect.ShowToast(it) }
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

    fun addSpace(name: String): Job = viewModelScope.launch {
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
                postSideEffect(ManageSpaceSideEffect.HaveManageResult)
            }.onErrorWithResult { errorWithResult ->
                val message = errorWithResult.errorResultEntity.message ?: return@launch
                postSideEffect(
                    message.let { ManageSpaceSideEffect.ShowToast(it) }
                )
            }
        }
    }

    fun editSpaceDialog(space: ManageSpaceEntity): Job = viewModelScope.launch {
        postSideEffect(ManageSpaceSideEffect.OpenEditSpaceDialog(space))
    }

    fun editSpace(spaceId: Long, spaceName: String) = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            runCatchingWithErrorHandler {
                manageSpaceRepository.editSpace(name = spaceName, spaceId = spaceId)
            }.onSuccess {space ->
                val editIndex = state.dataList.withIndex().first { indexedValue ->
                    indexedValue.value.id == space.id
                }.index
                setState(
                    state.copy(
                        dataList = state.dataList.toMutableList().apply {
                            this[editIndex] = space
                        }
                    )
                )
                postSideEffect(ManageSpaceSideEffect.HaveManageResult)
            }.onErrorWithResult { errorWithResult ->
                val message = errorWithResult.errorResultEntity.message ?: return@withState
                postSideEffect(
                    message.let { ManageSpaceSideEffect.ShowToast(it) }
                )
            }
        }
    }

    fun openDeleteSpaceDialog(spaceId: Long, spaceName: String) {
        postSideEffect(ManageSpaceSideEffect.OpenDeleteSpaceDialog(spaceId, spaceName))
    }

    fun deleteSpace(spaceId: Long): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            runCatchingWithErrorHandler {
                manageSpaceRepository.deleteSpace(spaceId)
            }.onSuccess {
                val deletedIndex = state.dataList.withIndex().first { indexedValue ->
                    indexedValue.value.id == spaceId
                }.index
                setState(
                    state.copy(
                        dataList = state.dataList.toMutableList().apply {
                            removeAt(deletedIndex)
                        }
                    )
                )
                postSideEffect(ManageSpaceSideEffect.HaveManageResult)
            }.onErrorWithResult { errorWithResult ->
                val message = errorWithResult.errorResultEntity.message ?: return@withState
                postSideEffect(
                    message.let { ManageSpaceSideEffect.ShowToast(it) }
                )
            }
        }
    }
}
