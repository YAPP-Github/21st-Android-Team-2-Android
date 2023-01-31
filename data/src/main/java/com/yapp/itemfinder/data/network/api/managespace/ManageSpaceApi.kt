package com.yapp.itemfinder.data.network.api.managespace

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ManageSpaceApi {
    @GET("/spaces")
    suspend fun fetchAllSpaces(): FetchAllSpaceResponse

    @POST("/spaces")
    suspend fun addNewSpace(@Body addSpaceRequest: AddSpaceRequest): SpaceResponse
}
