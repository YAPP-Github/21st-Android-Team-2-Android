package com.yapp.itemfinder.data.network.api.item

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemApi {
    @POST("/items/search")
    suspend fun searchItems(
        @Body itemSearchRequest: ItemSearchRequest,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): ItemSearchResponse

    @POST("/items")
    suspend fun createItem(@Body addItemRequest: AddItemRequest): AddItemResponse

    @PUT("/items/{itemId}")
    suspend fun editItem(
        @Path("itemId") itemId: Long,
        @Body addItemRequest: AddItemRequest
    ): AddItemResponse

    @GET("/items/{itemId}")
    suspend fun getItemById(@Path("itemId") itemId: Long): ItemDetailResponse

    @DELETE("/items/{itemId}")
    suspend fun deleteItem(@Path("itemId") itemId: Long)

}
