package com.yapp.itemfinder.data.network.api.managespace

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ManageSpaceApi {
    @GET("/spaces")
    suspend fun fetchAllSpaces(): Response<JsonObject>

    @POST("/spaces")
    suspend fun addNewSpace(@Body addSpaceRequest: AddSpaceRequest): AddSpaceResponse
}
