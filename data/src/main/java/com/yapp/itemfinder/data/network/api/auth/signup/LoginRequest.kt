package com.yapp.itemfinder.data.network.api.auth.signup

data class LoginRequest(
    val socialId: String,
    val socialType: String,
)
