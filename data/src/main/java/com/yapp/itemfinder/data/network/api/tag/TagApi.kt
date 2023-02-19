package com.yapp.itemfinder.data.network.api.tag

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TagApi {

    @GET("/tags")
    suspend fun fetchTags(): TagsResponse

    @POST("/tags")
    suspend fun createTags(@Body itemSearchRequest: CreateTagsRequest): TagsResponse

}
