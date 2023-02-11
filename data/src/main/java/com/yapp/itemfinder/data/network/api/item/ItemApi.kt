package com.yapp.itemfinder.data.network.api.item

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ItemApi {

    @POST("/items")
    suspend fun addNewItem(@Body addItemRequest:AddItemRequest):  AddItemResponse
}
