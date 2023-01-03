package com.yapp.itemfinder.data.network.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    @GET("/")
    suspend fun fetchHealthCheck(): Response<JsonObject>

}
