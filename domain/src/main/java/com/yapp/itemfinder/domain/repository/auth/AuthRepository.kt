package com.yapp.itemfinder.domain.repository.auth

import com.yapp.itemfinder.domain.entity.auth.AuthTokenEntity
import com.yapp.itemfinder.domain.entity.signup.SignUpEntity
import com.yapp.itemfinder.domain.type.SocialType

interface AuthRepository {

    suspend fun getAuthToken(): AuthTokenEntity

    suspend fun putAuthTokenToPreference(token: AuthTokenEntity)

    suspend fun refreshAuthToken()

    suspend fun validateMember(token: AuthTokenEntity)

    suspend fun loginUser(socialId: String, socialType: SocialType): AuthTokenEntity

    suspend fun signUpUser(signUpEntity: SignUpEntity): AuthTokenEntity

}
