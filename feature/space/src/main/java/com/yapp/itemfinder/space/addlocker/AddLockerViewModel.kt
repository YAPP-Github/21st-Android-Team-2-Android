package com.yapp.itemfinder.space.addlocker

import com.yapp.itemfinder.domain.model.AddLockerNameInput
import com.yapp.itemfinder.domain.model.AddLockerPhoto
import com.yapp.itemfinder.domain.model.AddLockerSpace
import com.yapp.itemfinder.domain.model.LockerIcons
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddLockerViewModel @Inject constructor(

) : BaseStateViewModel<AddLockerState, AddLockerSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddLockerState> =
        MutableStateFlow(AddLockerState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddLockerSideEffect> = MutableSharedFlow()

    init {
        setState(
            AddLockerState.Success(
                listOf(
                    AddLockerNameInput(),
                    AddLockerSpace(name = "옷장"),
                    LockerIcons(),
                    AddLockerPhoto()
                )
            )
        )
    }

    fun openSelectSpace() {
        withState<AddLockerState.Success> {
            postSideEffect(AddLockerSideEffect.OpenSelectSpace)
        }
    }

    fun changeSpace(name: String) {
        withState<AddLockerState.Success> { state ->
            setState(
                state.copy(
                    dataList = ArrayList(state.dataList).apply {
                        removeAt(1)
                        add(1, AddLockerSpace(name = name))
                    }
                )
            )
        }
    }
}
