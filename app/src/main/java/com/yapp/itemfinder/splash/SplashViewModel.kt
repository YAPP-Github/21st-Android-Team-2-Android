package com.yapp.itemfinder.splash

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.repository.AppRepository
import com.yapp.itemfinder.domain.repository.auth.AuthRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.exception.DefaultErrorHandler
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val authRepository: AuthRepository
) : BaseStateViewModel<SplashScreenState, SplashScreenSideEffect>() {

    override val _stateFlow: MutableStateFlow<SplashScreenState> = MutableStateFlow(SplashScreenState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SplashScreenSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        runCatchingWithErrorHandler {
            val healthCheckDeferred = async { appRepository.fetchHealthCheck() }
            val authTokenDeferred = async { authRepository.getAuthToken() }
            val (_, authToken) = healthCheckDeferred.await() to authTokenDeferred.await()

            delay(2000)
            authRepository.validateMember(authToken)
        }.onSuccess {
            postSideEffect(
                SplashScreenSideEffect.StartHome
            )
        }.onErrorWithResult {
            when {
                DefaultErrorHandler.isTokenExpiredException(it.throwable) -> {
                    postSideEffect(
                        SplashScreenSideEffect.StartPrelogin
                    )
                }
                else -> postSideEffect(SplashScreenSideEffect.ShowErrorToast(it.errorResultEntity.message))
            }
        }
    }
}
