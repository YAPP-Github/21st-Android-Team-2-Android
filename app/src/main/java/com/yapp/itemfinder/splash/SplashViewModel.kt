package com.yapp.itemfinder.splash

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.repository.AppRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : BaseStateViewModel<SplashScreenState, SplashScreenSideEffect>() {

    override val _stateFlow: MutableStateFlow<SplashScreenState> =MutableStateFlow(SplashScreenState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SplashScreenSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        runCatchingWithErrorHandler {
            appRepository.fetchHealthCheck()
        }.onSuccess {
            delay(2000)
            postSideEffect(
                SplashScreenSideEffect.StartHome
            )
        }.onErrorWithResult {
            postSideEffect(
                SplashScreenSideEffect.StartSignUp
            )
        }
    }

}
