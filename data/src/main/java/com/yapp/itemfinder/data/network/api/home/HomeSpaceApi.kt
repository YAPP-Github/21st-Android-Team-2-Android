package com.yapp.itemfinder.data.network.api.home

import retrofit2.Response
import retrofit2.http.GET

interface HomeSpaceApi {
    @GET("/spaces/containers")
    suspend fun fetchHomeSpaces(): Response<List<HomeSpaceResponse>>
}
