package com.yapp.itemfinder.data.network.api.auth.login

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String
)
