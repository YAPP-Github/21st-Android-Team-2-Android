package com.yapp.itemfinder.data.repositories.auth

import com.yapp.itemfinder.data.network.api.auth.AuthApi
import com.yapp.itemfinder.data.network.api.auth.AuthWithoutTokenApi
import com.yapp.itemfinder.data.network.api.auth.login.SignUpRequest
import com.yapp.itemfinder.data.network.api.auth.signup.LoginRequest
import com.yapp.itemfinder.domain.data.SecureLocalData
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import com.yapp.itemfinder.domain.entity.auth.AuthTokenEntity
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.entity.signup.SignUpEntity
import com.yapp.itemfinder.domain.repository.auth.AuthRepository
import com.yapp.itemfinder.domain.type.SocialType
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val secureLocalDataStore: SecureLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val authApi: AuthApi,
    private val authWithoutTokenApi: AuthWithoutTokenApi,
): AuthRepository {

    override suspend fun getAuthToken(): AuthTokenEntity = withContext(dispatcherProvider.io) {
        val accessToken = secureLocalDataStore.get(SecureLocalData.AccessToken)
        val refreshToken = secureLocalDataStore.get(SecureLocalData.RefreshToken)
        AuthTokenEntity(accessToken, refreshToken)
    }

    override suspend fun putAuthTokenToPreference(token: AuthTokenEntity) = withContext(dispatcherProvider.default) {
        val accessTokenJob = launch { secureLocalDataStore.put(SecureLocalData.AccessToken, token.accessToken) }
        launch { secureLocalDataStore.put(SecureLocalData.RefreshToken, token.refreshToken) }

        // SharedPreferences의 데이터를 업데이트한 이후 업데이트 signal 전파
        accessTokenJob.join()
    }

    override suspend fun putUserNickname(nickname: String) = withContext(dispatcherProvider.default) {
        secureLocalDataStore.put(SecureLocalData.Nickname, nickname)
    }

    override suspend fun getUserNickname(): String {
        return secureLocalDataStore.get(SecureLocalData.Nickname)
    }

    override suspend fun putUserSocialId(socialId: String) = withContext(dispatcherProvider.default) {
        secureLocalDataStore.put(SecureLocalData.SocialId, socialId)
    }

    override suspend fun getUserSocialId(): String {
        return secureLocalDataStore.get(SecureLocalData.SocialId)
    }

    override suspend fun refreshAuthToken() {

    }

    override suspend fun validateMember(token: AuthTokenEntity) {
        authApi.validateMember()
    }

    override suspend fun loginUser(socialId: String, socialType: SocialType): AuthTokenEntity =
        authWithoutTokenApi.login(LoginRequest(socialId, socialType.name)).toAuthTokenEntity()

    override suspend fun logoutUser() {
        authApi.logout()
    }

    override suspend fun signUpUser(signUpEntity: SignUpEntity): AuthTokenEntity =
        authWithoutTokenApi.signup(SignUpRequest.fromEntity(signUpEntity)).toAuthTokenEntity()

    override suspend fun clearAllPreference() {
        secureLocalDataStore.clear(SecureLocalData.RefreshToken.prefName)
        secureLocalDataStore.clear(SecureLocalData.AccessToken.prefName)
        secureLocalDataStore.clear(SecureLocalData.Nickname.prefName)
        secureLocalDataStore.clear(SecureLocalData.SocialId.prefName)
    }

}
