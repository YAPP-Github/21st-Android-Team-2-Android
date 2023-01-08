package com.yapp.itemfinder.data.network.api.auth.login

import com.yapp.itemfinder.domain.entity.auth.AuthTokenEntity

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String
) {

    fun toAuthTokenEntity() = AuthTokenEntity(accessToken, refreshToken)

}
