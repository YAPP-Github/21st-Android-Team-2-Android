package com.yapp.itemfinder.data.network.api.locker

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LockerApi {

    @GET("containers/{containerId}")
    suspend fun getLockerById(@Path("containerId") containerId: Long): LockerResponse
    @GET("containers/by-space-id/{spaceId}")
    suspend fun getLockersBySpaceId(@Path("spaceId") spaceId: Long): List<LockerResponse>

    @POST("containers")
    suspend fun addNewLocker(@Body locker: AddEditLockerRequest): LockerResponse

    @PUT("containers/{containerId}")
    suspend fun editLocker(
        @Body locker: AddEditLockerRequest,
        @Path("containerId") lockerId: Long
    ): LockerResponse

    @DELETE("containers/{containerId}")
    suspend fun deleteLocker(
        @Path("containerId") lockerId: Long
    )

}
