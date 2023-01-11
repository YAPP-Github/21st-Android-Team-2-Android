package com.yapp.itemfinder.space.managespace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
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
        val spaces = manageSpaceRepository.getAllManageSpaceItems()
        setState(
            ManageSpaceState.Success(
                dataList = listOf(AddSpace()) + spaces
            )
        )
    }

    fun openAddSpaceDialog(): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            postSideEffect(
                ManageSpaceSideEffect.OpenAddSpaceDialog
            )
        }
    }

    fun addItem(name: String): Job = viewModelScope.launch {

    }

    fun editItem(space: ManageSpaceItem): Job = viewModelScope.launch {

    }

    fun deleteItem(space: ManageSpaceItem): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            postSideEffect(
                ManageSpaceSideEffect.DeleteDialog
            )
        }
    }
}
