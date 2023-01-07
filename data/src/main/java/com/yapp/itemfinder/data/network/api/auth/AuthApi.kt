package com.yapp.itemfinder.data.network.api.auth

import retrofit2.http.GET

interface AuthApi {

    @GET("/auth/validate-member")
    suspend fun validateMember()

    @GET("/auth/logout")
    suspend fun logout()

}
