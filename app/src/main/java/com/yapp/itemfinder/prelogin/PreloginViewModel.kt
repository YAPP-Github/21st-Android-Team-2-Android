package com.yapp.itemfinder.prelogin

import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PreloginViewModel @Inject constructor(

) : BaseStateViewModel<PreloginState, PreloginSideEffect>() {

    override val _stateFlow: MutableStateFlow<PreloginState>
        get() = MutableStateFlow(PreloginState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<PreloginSideEffect>
        get() = MutableSharedFlow()



}
