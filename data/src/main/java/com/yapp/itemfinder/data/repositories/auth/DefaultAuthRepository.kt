package com.yapp.itemfinder.data.repositories.auth

import com.yapp.itemfinder.data.network.api.auth.AuthApi
import com.yapp.itemfinder.data.network.api.auth.AuthWithoutTokenApi
import com.yapp.itemfinder.domain.data.SecureLocalData
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import com.yapp.itemfinder.domain.model.auth.AuthToken
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val secureLocalDataStore: SecureLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val authApi: AuthApi,
    private val authWithoutTokenApi: AuthWithoutTokenApi,
): AuthRepository {

    override suspend fun getAuthToken(): AuthToken = withContext(dispatcherProvider.io) {
        val accessToken = secureLocalDataStore.get(SecureLocalData.AccessToken)
        val refreshToken = secureLocalDataStore.get(SecureLocalData.RefreshToken)
        AuthToken(accessToken, refreshToken)
    }

    override suspend fun putAuthTokenToPreference(token: AuthToken) = withContext(dispatcherProvider.default) {
        val accessTokenJob = launch { secureLocalDataStore.put(SecureLocalData.AccessToken, token.accessToken) }
        launch { secureLocalDataStore.put(SecureLocalData.RefreshToken, token.refreshToken) }

        // SharedPreferences의 데이터를 업데이트한 이후 업데이트 signal 전파
        accessTokenJob.join()
    }

    override suspend fun refreshAuthToken() {

    }

    override suspend fun validateMember(token: AuthToken) {

    }

}
