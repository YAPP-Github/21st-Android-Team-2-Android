package com.yapp.itemfinder.prelogin

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.entity.signup.SignUpEntity
import com.yapp.itemfinder.domain.repository.auth.AuthRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.exception.DefaultErrorHandler
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreloginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseStateViewModel<PreloginState, PreloginSideEffect>() {

    override val _stateFlow: MutableStateFlow<PreloginState> = MutableStateFlow(PreloginState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<PreloginSideEffect> = MutableSharedFlow()

    fun requestKakaoLogin() {
        setState(PreloginState.Loading)
        postSideEffect(PreloginSideEffect.RequestKakaoLogin)
    }

    fun requestSignIn(
        signUpEntity: SignUpEntity
    ) = viewModelScope.launch {
        runCatchingWithErrorHandler {
            setState(PreloginState.Loading)
            authRepository.putUserNickname(signUpEntity.nickname)
            authRepository.putUserSocialId(signUpEntity.socialId)
            authRepository.loginUser(signUpEntity.socialId, signUpEntity.socialType)
        }.onSuccess { authToken ->
            authRepository.putAuthTokenToPreference(authToken)
            setState(PreloginState.Success)
            Log.d("authToken", authToken.toString())
            postSideEffect(PreloginSideEffect.StartHome)
        }.onErrorWithResult { errorWithResult ->
            when {
                DefaultErrorHandler.isApiNotFoundException(errorWithResult.throwable) -> {
                    runCatchingWithErrorHandler {
                        authRepository.signUpUser(signUpEntity)
                    }.onSuccess { authToken ->
                        authRepository.putAuthTokenToPreference(authToken)
                        setState(PreloginState.Success)
                    }.onErrorWithResult {
                        setState(PreloginState.Error)
                        val message = it.errorResultEntity.message ?: return@launch
                        postSideEffect(PreloginSideEffect.ShowToast(message))
                    }
                }
                else -> {
                    val message = errorWithResult.errorResultEntity.message ?: return@launch
                    postSideEffect(PreloginSideEffect.ShowToast(message))
                }
            }
        }
    }

}
