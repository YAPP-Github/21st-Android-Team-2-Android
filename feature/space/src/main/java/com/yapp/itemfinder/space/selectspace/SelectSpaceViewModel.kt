package com.yapp.itemfinder.space.selectspace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.SpaceRepositoryQualifiers
import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.addlocker.AddLockerActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSpaceViewModel @Inject constructor(
    @SpaceRepositoryQualifiers
    private val spaceRepository: SpaceRepository,
    private val savedStateHandle: SavedStateHandle
) :
    BaseStateViewModel<SelectSpaceState, SelectSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<SelectSpaceState> =
        MutableStateFlow(SelectSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SelectSpaceSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(SelectSpaceState.Loading)

        runCatchingWithErrorHandler {
            spaceRepository.getAllSpaces()
        }.onSuccess { spaces ->
            val spaceId = savedStateHandle.get<Long>(AddLockerActivity.SPACE_ID_KEY) ?: 0
            val checkedIndex = spaces.indexOf(spaces.firstOrNull { it.id == spaceId })
            spaces[checkedIndex].isChecked = true
            setState(
                SelectSpaceState.Success(
                    dataList = spaces,
                    spaceId = spaceId,
                    spaceName = "",
                    checkedIndex = checkedIndex
                )
            )
        }.onErrorWithResult { errorWithResult ->
            setState(SelectSpaceState.Error(errorWithResult))
            val message = errorWithResult.errorResultEntity.message
            message?.let { postSideEffect(SelectSpaceSideEffect.ShowToast(it)) }
        }
    }

    fun changeChecked(selectSpace: SelectSpace) = withState<SelectSpaceState.Success> { state ->
        val clickedIndex = state.dataList.indexOf(selectSpace)
        val currentIndex = state.checkedIndex
        if (clickedIndex == currentIndex) {
            return@withState
        }
        val newDataList = ArrayList(state.dataList)
        newDataList[currentIndex] =
            (newDataList[currentIndex] as SelectSpace).copy(isChecked = false)
        newDataList[clickedIndex] =
            (newDataList[clickedIndex] as SelectSpace).copy(isChecked = true)
        setState(
            SelectSpaceState.Success(
                newDataList,
                newDataList[clickedIndex].id,
                (newDataList[clickedIndex] as SelectSpace).name,
                clickedIndex
            )
        )
    }

    fun getNewSpaceId(): Long {
        var id = 0L
        withState<SelectSpaceState.Success> { state ->
            id = state.spaceId
        }
        return id
    }

    fun getNewSpaceName(): String {
        var name = ""
        withState<SelectSpaceState.Success> { state ->
            name = state.spaceName
        }
        return name
    }

}
