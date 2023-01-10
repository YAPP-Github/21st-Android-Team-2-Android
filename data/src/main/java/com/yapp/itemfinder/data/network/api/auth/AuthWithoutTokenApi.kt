package com.yapp.itemfinder.data.network.api.auth

import com.yapp.itemfinder.data.network.api.auth.login.SignUpRequest
import com.yapp.itemfinder.data.network.api.auth.login.SignUpResponse
import com.yapp.itemfinder.data.network.api.auth.signup.LoginRequest
import com.yapp.itemfinder.data.network.api.auth.signup.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthWithoutTokenApi {

    @POST("/auth/signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): SignUpResponse

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

}
