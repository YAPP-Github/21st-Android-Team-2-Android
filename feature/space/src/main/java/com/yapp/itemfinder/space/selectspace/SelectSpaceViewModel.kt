package com.yapp.itemfinder.space.selectspace

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.SelectSpace
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
                dataList = listOf(
                    SelectSpace(id = 1, name = "서재"),
                    SelectSpace(id = 2, name = "주방"),
                    SelectSpace(id = 3, name = "작업실"),
                    SelectSpace(id = 4, name = "베란다"),
                    SelectSpace(id = 5, name = "드레스룸")
                )
            )
        )
    }
}
