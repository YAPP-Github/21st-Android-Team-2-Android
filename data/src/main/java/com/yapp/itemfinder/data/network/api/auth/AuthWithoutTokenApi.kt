package com.yapp.itemfinder.data.network.api.auth

import com.yapp.itemfinder.data.network.api.auth.login.SignUpRequest
import com.yapp.itemfinder.data.network.api.auth.login.SignUpResponse
import com.yapp.itemfinder.data.network.api.auth.signup.LoginRequest
import com.yapp.itemfinder.data.network.api.auth.signup.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthWithoutTokenApi {

    @POST("/auth/signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @GET("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

}
