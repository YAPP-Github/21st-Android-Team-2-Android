package com.yapp.itemfinder.space.selectspace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SelectSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSpaceViewModel @Inject constructor(
    private val selectSpaceMockRepository: SelectSpaceRepository
) :
    BaseStateViewModel<SelectSpaceState, SelectSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<SelectSpaceState> =
        MutableStateFlow(SelectSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SelectSpaceSideEffect> = MutableSharedFlow()

    private var checkedIndex = 0

    override fun fetchData(): Job = viewModelScope.launch {
        setState(SelectSpaceState.Loading)
        val spaces = selectSpaceMockRepository.getAllSpaces()
        setState(
            SelectSpaceState.Success(
                dataList = spaces
            )
        )
        withState<SelectSpaceState.Success> { state ->
            checkedIndex = spaces.indexOf(spaces.firstOrNull { it.isChecked })
        }
    }

    fun changeChecked(selectSpace: SelectSpace) = withState<SelectSpaceState.Success> { state ->
        val clickedIndex = state.dataList.indexOf(selectSpace)
        if (clickedIndex == checkedIndex) {
            return@withState
        }
        val newDataList = ArrayList(state.dataList)
        val old = (newDataList[checkedIndex] as SelectSpace).copy(isChecked = false)
        newDataList.removeAt(checkedIndex)
        newDataList.add(checkedIndex, old)
        checkedIndex = clickedIndex
        val new = (newDataList[clickedIndex] as SelectSpace).copy(isChecked = true)
        newDataList.removeAt(clickedIndex)
        newDataList.add(clickedIndex, new)
        setState(
            SelectSpaceState.Success(newDataList)
        )
    }
}
