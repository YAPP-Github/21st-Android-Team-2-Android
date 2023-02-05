package com.yapp.itemfinder.data.network.api.lockerlist

import com.yapp.itemfinder.domain.model.AddLockerRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LockerApi {
    @GET("containers/by-space-id/{spaceId}")
    suspend fun getLockersBySpaceId(@Path("spaceId") spaceId: Long): List<LockerResponse>

    @POST("containers")
    suspend fun addNewLocker(@Body locker: AddLockerRequest): LockerResponse
}
