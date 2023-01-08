package com.yapp.itemfinder.domain.entity.auth

data class AuthTokenEntity(
    val accessToken: String,
    val refreshToken: String,
)
