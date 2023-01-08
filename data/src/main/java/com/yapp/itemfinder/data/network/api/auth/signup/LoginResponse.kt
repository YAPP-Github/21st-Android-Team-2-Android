package com.yapp.itemfinder.data.network.api.auth.signup

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
