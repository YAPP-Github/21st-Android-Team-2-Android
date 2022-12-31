package com.yapp.itemfinder.feature.common

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseStateViewModel<S : State, SE : SideEffect> : BaseViewModel() {

    protected abstract val _stateFlow: MutableStateFlow<S>
    val stateFlow: StateFlow<S>
        get() = _stateFlow

    protected abstract val _sideEffectFlow: MutableSharedFlow<SE>
    val sideEffectFlow: SharedFlow<SE>
        get() = _sideEffectFlow

    protected inline fun <reified S : State> withState(accessState: (S) -> Unit): Boolean {
        if (stateFlow.value is S) {
            accessState(stateFlow.value as S)
            return true
        }
        return false
    }

    protected fun setState(state: S) {
        _stateFlow.value = state
    }

    protected fun postSideEffect(sideEffect: SE) = viewModelScope.launch {
        _sideEffectFlow.emit(sideEffect)
    }

}
