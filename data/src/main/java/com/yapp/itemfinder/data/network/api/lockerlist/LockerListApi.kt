package com.yapp.itemfinder.data.network.api.lockerlist

import retrofit2.http.GET
import retrofit2.http.Path

interface LockerListApi {
    @GET("containers/by-space-id/{spaceId}")
    suspend fun getLockersBySpaceId(@Path("spaceId") spaceId: Long): List<LockerResponse>
}
