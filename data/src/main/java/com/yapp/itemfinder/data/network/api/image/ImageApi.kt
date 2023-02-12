package com.yapp.itemfinder.data.network.api.image

import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @POST("images")
    @Headers("Accept: */*")
    @Multipart
    suspend fun addImages(@Part images: List<MultipartBody.Part>): List<ImageResponse>
}
