package com.yapp.itemfinder.data.network.api.auth

import retrofit2.Response
import retrofit2.http.GET

interface AuthApi {

    @GET("/auth/validate-member")
    suspend fun validateMember(): Response<Unit>

    @GET("/auth/logout")
    suspend fun logout(): Response<Unit>

}
