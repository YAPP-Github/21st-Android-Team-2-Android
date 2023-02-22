package com.yapp.itemfinder.data.network.api.managespace

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ManageSpaceApi {
    @GET("/spaces")
    suspend fun fetchAllSpaces(): FetchAllSpaceResponse

    @POST("/spaces")
    suspend fun addNewSpace(@Body addSpaceRequest: AddSpaceRequest): SpaceResponse

    @PUT("/spaces/{spaceId}")
    suspend fun editSpace(
        @Path("spaceId") spaceId: Long,
        @Body editSpaceRequest: AddSpaceRequest
    ): SpaceResponse

    @DELETE("spaces/{spaceId}")
    suspend fun deleteSpace(@Path("spaceId") spaceId: Long)
    
}
