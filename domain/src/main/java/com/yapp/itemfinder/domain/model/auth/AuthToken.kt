package com.yapp.itemfinder.domain.model.auth

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
