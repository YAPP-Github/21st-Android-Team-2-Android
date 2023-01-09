package com.yapp.itemfinder.domain.repository.auth

import com.yapp.itemfinder.domain.model.auth.AuthToken

interface AuthRepository {

    suspend fun getAuthToken(): AuthToken

    suspend fun putAuthTokenToPreference(token: AuthToken)

    suspend fun refreshAuthToken()

    suspend fun validateMember(token: AuthToken)

}
