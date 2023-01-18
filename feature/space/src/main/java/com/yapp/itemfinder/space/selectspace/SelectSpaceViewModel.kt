package com.yapp.itemfinder.space.selectspace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.SelectSpace
import com.yapp.itemfinder.domain.repository.SelectSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectSpaceViewModel @Inject constructor(
    private val selectSpaceMockRepository: SelectSpaceRepository
) :
    BaseStateViewModel<SelectSpaceState, SelectSpaceSideEffect>() {
    override val _stateFlow: MutableStateFlow<SelectSpaceState> =
        MutableStateFlow(SelectSpaceState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SelectSpaceSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(SelectSpaceState.Loading)
        // val spaces = withContext(Dispatchers.IO) {  }
        setState(
            SelectSpaceState.Success(
                dataList = selectSpaceMockRepository.getAllSpaces()
            )
        )
    }
}
