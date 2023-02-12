package com.yapp.itemfinder.data.network.api.item

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemApi {
    @POST("/items/search")
    suspend fun searchItems(
        @Body itemSearchRequest: ItemSearchRequest,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ItemSearchResponse
}
