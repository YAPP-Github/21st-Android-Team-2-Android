package com.yapp.itemfinder.data.network.di.okhttp

import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yapp.itemfinder.data.network.mapper.DataMapper
import com.yapp.itemfinder.data.network.response.ErrorResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class OkHttpInterceptorModule {

    @HttpLoggingInterceptorQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @DataParseInterceptorQualifier
    @Provides
    fun provideDataParseInterceptorQualifier(
        dataMapper: DataMapper
    ): Interceptor = Interceptor { chain ->
        val gson = Gson()
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        val response = chain.proceed(request)
        val responseData = if (response.isSuccessful) {
            val rawJson = response.body?.string() ?: "{}"
            val jsonObject = gson.fromJson(rawJson, JsonObject::class.java)
            if (jsonObject.isJsonArray) {
                /**
                 * [
                 *   {id, type=food},
                 *   {id, type=필통},
                 *   {id, type},
                 *   {id, type},
                 * ]
                 */
                jsonObject.asJsonArray.map {
                    dataMapper.map(it.asJsonObject)
                }
            } else {
                /**
                 * {id, type}
                 */
                dataMapper.map(jsonObject) ?: jsonObject
            }
        } else {
            ErrorResponse(
                code = response.code,
                message = response.message,
            )
        }

        response.newBuilder()
            .body(gson.toJson(responseData).toResponseBody())
            .build()
    }
}

