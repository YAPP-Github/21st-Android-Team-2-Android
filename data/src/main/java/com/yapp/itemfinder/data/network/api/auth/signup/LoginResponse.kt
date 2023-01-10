package com.yapp.itemfinder.data.network.api.auth.signup

import com.yapp.itemfinder.domain.entity.auth.AuthTokenEntity

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
) {

    fun toAuthTokenEntity() = AuthTokenEntity(accessToken, refreshToken)

}
