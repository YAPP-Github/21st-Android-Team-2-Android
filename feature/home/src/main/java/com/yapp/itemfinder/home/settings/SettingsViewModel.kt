package com.yapp.itemfinder.home.settings

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.provider.BuildConfigProvider
import com.yapp.itemfinder.domain.provider.UrlProvider
import com.yapp.itemfinder.domain.repository.AppRepository
import com.yapp.itemfinder.domain.repository.auth.AuthRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.LockerListSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val buildConfigProvider: BuildConfigProvider,
    private val urlProvider: UrlProvider,
    private val authRepository: AuthRepository
) : BaseStateViewModel<SettingsScreenState, SettingsScreenSideEffect>() {

    override val _stateFlow: MutableStateFlow<SettingsScreenState> = MutableStateFlow(SettingsScreenState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<SettingsScreenSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            SettingsScreenState.Success(
                appVersion = buildConfigProvider.versionName
            )
        )
    }

    fun movePrivacyGuidance() {
        postSideEffect(
            SettingsScreenSideEffect.Start.PrivacyHandle(
                urlProvider.getPrivacyHandleUrl()
            )
        )
    }

    fun moveServiceTermsOfUse() {
        postSideEffect(
            SettingsScreenSideEffect.Start.TermsOfUse(
                urlProvider.getTermsOfUseUrl()
            )
        )
    }

    fun moveOpenSource() {
        postSideEffect(
            SettingsScreenSideEffect.Start.OpenSource
        )
    }

    fun moveContactUs() = viewModelScope.launch {
        postSideEffect(
            SettingsScreenSideEffect.Start.ContactUs(
                email = "yappteam2022@gmail.com",
                subject = "[문의] ${authRepository.getUserNickname()}님이 문의합니다.",
                text =
                """
문의 유형 : ( 계정 관리 / 서비스 이용 / 오류 신고 / 기타 중 택일 )
문의 내용 : 

파인드띵스 아이디 : ${authRepository.getUserSocialId()}
파인드띵스 앱 버전 : ${buildConfigProvider.versionName}
이용하는 디바이스 : ${buildConfigProvider.deviceModel}
디바이스 버전 : ${buildConfigProvider.deviceOs}

가능한 신속히 답변을 드리겠습니다.
                """.trimIndent(),
            )
        )
    }

    fun logout() = viewModelScope.launch {
        runCatchingWithErrorHandler {
            postSideEffect(
                SettingsScreenSideEffect.ShowLoading(
                    isShow = true
                )
            )
            authRepository.logoutUser()
            authRepository.clearAllPreference()
            postSideEffect(
                SettingsScreenSideEffect.ShowLoading(
                    isShow = false
                )
            )
            postSideEffect(SettingsScreenSideEffect.Start.ClearAndGoToPreLogin)
        }.onErrorWithResult { errorWithResult ->
            val message = errorWithResult.errorResultEntity.message
            message?.let { postSideEffect(SettingsScreenSideEffect.ShowErrorToast(message)) }
        }
    }
}
