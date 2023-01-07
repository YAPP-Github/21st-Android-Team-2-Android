package com.yapp.itemfinder.data.network.api.auth.login

data class SignUpRequest(
    val socialId: String,
    val socialType: String,
    val nickname: String?,
    val email: String?,
    val gender: String?,
    val birthYear: Int?
)
