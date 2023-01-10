package com.yapp.itemfinder.space.managespace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddSpace
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageSpaceViewModel @Inject constructor(

) : BaseStateViewModel<ManageSpaceState, ManageSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<ManageSpaceState> =
        MutableStateFlow(ManageSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<ManageSpaceSideEffect> = MutableSharedFlow()

    val mockManageSpaceItems = listOf<Data>(
        AddSpace(),
        ManageSpaceItem(name = "서재"),
        ManageSpaceItem(name = "주방"),
        ManageSpaceItem(name = "드레스룸"),
        ManageSpaceItem(name = "작업실"),
        ManageSpaceItem(name = "베란다")
    )

    override fun fetchData(): Job = viewModelScope.launch {
        setState(ManageSpaceState.Loading)
        // space 목록은 home 에서 전달 예정
        setState(
            ManageSpaceState.Success(
                dataList = mockManageSpaceItems
            )
        )
    }

    fun addItem(): Job = viewModelScope.launch {
        withState<ManageSpaceState.Success> { state ->
            setState(
                state.copy(
                    ArrayList(state.dataList).apply {
                        // add()
                    }
                )
            )
        }
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
