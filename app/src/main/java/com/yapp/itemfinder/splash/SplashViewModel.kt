package com.yapp.itemfinder.splash

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashScreenRepository: SplashScreenRepository
) : BaseStateViewModel<SplashScreenState, SplashScreenSideEffect>() {

    override val _stateFlow: MutableStateFlow<SplashScreenState> =
        MutableStateFlow<SplashScreenState>(SplashScreenState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SplashScreenSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(SplashScreenState.Loading)
        splashScreenRepository.getAppData()
        setState(SplashScreenState.Success)
    }

    fun startHome(): Job = viewModelScope.launch {
        withState<SplashScreenState.Success> { state ->
            postSideEffect(
                SplashScreenSideEffect.StartHome
            )
        }
    }

}
