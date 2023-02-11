package com.yapp.itemfinder.data.network.api.item

import retrofit2.http.Body
import retrofit2.http.POST

interface ItemApi {
    @POST("/items/search")
    suspend fun searchItems(@Body itemSearchRequest: ItemSearchRequest): ItemSearchResponse
}
